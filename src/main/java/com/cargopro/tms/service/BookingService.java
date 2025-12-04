package com.cargopro.tms.service;

import com.cargopro.tms.dto.BookingResponse;
import com.cargopro.tms.entity.*;
import com.cargopro.tms.exception.InsufficientCapacityException;
import com.cargopro.tms.exception.InvalidStatusTransitionException;
import com.cargopro.tms.exception.LoadAlreadyBookedException;
import com.cargopro.tms.exception.ResourceNotFoundException;
import com.cargopro.tms.repository.BidRepository;
import com.cargopro.tms.repository.BookingRepository;
import com.cargopro.tms.repository.LoadRepository;
import com.cargopro.tms.repository.TransporterRepository;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing Bookings.
 * Handles the complex logic of accepting bids, managing capacity, and handling concurrency.
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterRepository transporterRepository;

    public BookingService(BookingRepository bookingRepository, BidRepository bidRepository, LoadRepository loadRepository, TransporterRepository transporterRepository) {
        this.bookingRepository = bookingRepository;
        this.bidRepository = bidRepository;
        this.loadRepository = loadRepository;
        this.transporterRepository = transporterRepository;
    }

    /**
     * Accepts a bid and creates a booking.
     * This method is transactional and handles optimistic locking.
     *
     * @param bidId           The ID of the bid to accept.
     * @param allocatedTrucks The number of trucks to allocate for this booking.
     * @param finalRate       The final agreed rate.
     * @return The created booking response.
     */
    @Transactional
    public BookingResponse acceptBidAndCreateBooking(UUID bidId, int allocatedTrucks, double finalRate) {
        // 1. Initial Fetch
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + bidId));
        
        Load load = loadRepository.findById(bid.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + bid.getLoadId()));
        
        Transporter transporter = transporterRepository.findById(bid.getTransporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + bid.getTransporterId()));

        // 2. Atomic Checks

        // Rule 3 (Truck Check): Check if allocatedTrucks doesn't exceed remaining required trucks.
        List<Booking> confirmedBookings = bookingRepository.findByLoadIdAndStatus(load.getLoadId(), BookingStatus.CONFIRMED);
        int currentlyAllocated = confirmedBookings.stream().mapToInt(Booking::getAllocatedTrucks).sum();
        int remainingRequired = load.getNumOfTrucks() - currentlyAllocated;

        if (allocatedTrucks > remainingRequired) {
            throw new InsufficientCapacityException("Cannot allocate " + allocatedTrucks + " trucks. Only " + remainingRequired + " needed for this load.");
        }

        // Rule 1 (Capacity Check): Re-validate transporter capacity.
        String requiredTruckType = load.getTruckType();
        TransporterTruckCapacity truckCapacity = transporter.getAvailableTrucks().stream()
                .filter(t -> t.getTruckType().equalsIgnoreCase(requiredTruckType))
                .findFirst()
                .orElseThrow(() -> new InsufficientCapacityException("Transporter has no capacity record for " + requiredTruckType));

        if (truckCapacity.getCount() < allocatedTrucks) {
            throw new InsufficientCapacityException("Transporter does not have enough " + requiredTruckType + 
                                                    " trucks. Available: " + truckCapacity.getCount() + 
                                                    ", Requested: " + allocatedTrucks);
        }

        // 3. Success Logic

        // Set Bid status to ACCEPTED
        bid.setStatus(BidStatus.ACCEPTED);
        bidRepository.save(bid);

        // Create and save Booking
        Booking booking = new Booking();
        booking.setLoadId(load.getLoadId());
        booking.setBidId(bid.getBidId());
        booking.setTransporterId(transporter.getTransporterId());
        booking.setAllocatedTrucks(allocatedTrucks);
        booking.setFinalRate(finalRate);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookedAt(Timestamp.from(Instant.now()));
        Booking savedBooking = bookingRepository.save(booking);

        // Rule 1 (Deduct Trucks): Deduct allocatedTrucks from Transporter's capacity.
        truckCapacity.setCount(truckCapacity.getCount() - allocatedTrucks);
        transporterRepository.save(transporter);

        // Rule 3 (Status Transition): Update Load status if fully booked.
        if (currentlyAllocated + allocatedTrucks == load.getNumOfTrucks()) {
            load.setStatus(LoadStatus.BOOKED);
        } else {
            // Ensure it's OPEN_FOR_BIDS if partially booked (it might have been POSTED)
            load.setStatus(LoadStatus.OPEN_FOR_BIDS);
        }

        // Rule 4 (Concurrency): Handle Optimistic Locking
        try {
            loadRepository.save(load);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new LoadAlreadyBookedException("Load was modified or booked by another transaction. Please refresh and try again.");
        }

        return mapEntityToResponse(savedBooking);
    }

    /**
     * Cancels a booking.
     * Restores truck capacity and updates load status.
     *
     * @param bookingId The ID of the booking to cancel.
     * @return The updated booking response.
     */
    @Transactional
    public BookingResponse cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Booking is already cancelled.");
        }

        Load load = loadRepository.findById(booking.getLoadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + booking.getLoadId()));

        Transporter transporter = transporterRepository.findById(booking.getTransporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + booking.getTransporterId()));

        // Rule 1 (Restore Trucks): Add allocatedTrucks back to Transporter's capacity.
        String requiredTruckType = load.getTruckType();
        TransporterTruckCapacity truckCapacity = transporter.getAvailableTrucks().stream()
                .filter(t -> t.getTruckType().equalsIgnoreCase(requiredTruckType))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Capacity record not found for restoration.")); // Should ideally not happen

        truckCapacity.setCount(truckCapacity.getCount() + booking.getAllocatedTrucks());
        transporterRepository.save(transporter);

        // Rule 2 (Status Transition): Set Booking to CANCELLED.
        booking.setStatus(BookingStatus.CANCELLED);
        Booking savedBooking = bookingRepository.save(booking);

        // Update Load status
        // If the load was BOOKED, it should now be OPEN_FOR_BIDS as capacity is freed up.
        // If it was already OPEN_FOR_BIDS (partial booking), it stays OPEN_FOR_BIDS.
        if (load.getStatus() == LoadStatus.BOOKED) {
            load.setStatus(LoadStatus.OPEN_FOR_BIDS);
            loadRepository.save(load);
        }
        
        // Note: If all bookings are cancelled, we might want to set it back to POSTED or OPEN_FOR_BIDS.
        // The requirement says "revert... back to OPEN_FOR_BIDS", which covers both cases effectively for bidding.

        return mapEntityToResponse(savedBooking);
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId The ID of the booking.
     * @return The booking response.
     */
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        return mapEntityToResponse(booking);
    }

    private BookingResponse mapEntityToResponse(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getLoadId(),
                booking.getBidId(),
                booking.getTransporterId(),
                booking.getAllocatedTrucks(),
                booking.getFinalRate(),
                booking.getStatus(),
                booking.getBookedAt()
        );
    }
}
