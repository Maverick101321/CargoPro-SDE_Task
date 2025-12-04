package com.cargopro.tms.service;

import com.cargopro.tms.dto.BidRequest;
import com.cargopro.tms.dto.BidResponse;
import com.cargopro.tms.entity.*;
import com.cargopro.tms.exception.InsufficientCapacityException;
import com.cargopro.tms.exception.InvalidStatusTransitionException;
import com.cargopro.tms.exception.ResourceNotFoundException;
import com.cargopro.tms.repository.BidRepository;
import com.cargopro.tms.repository.LoadRepository;
import com.cargopro.tms.repository.TransporterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * Service class for managing Bids.
 * Handles bid submission, rejection, and retrieval.
 */
@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterRepository transporterRepository;

    /**
     * Submits a new bid for a load.
     *
     * @param request The bid details.
     * @return The created bid response.
     */
    @Transactional
    public BidResponse submitBid(BidRequest request) {
        // 1. Fetch Load and Transporter
        Load load = loadRepository.findById(request.loadId())
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + request.loadId()));

        Transporter transporter = transporterRepository.findById(request.transporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + request.transporterId()));

        // 2. Rule 2 (Bid Status Check): Check if the Load's status is valid for bidding.
        // Bids can be placed if status is POSTED or OPEN_FOR_BIDS.
        if (load.getStatus() == LoadStatus.BOOKED || load.getStatus() == LoadStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Cannot bid on a load that is " + load.getStatus());
        }

        // 3. Rule 1 (Capacity Check): Check if transporter has enough trucks of the required type.
        String requiredTruckType = load.getTruckType();
        int availableCapacity = transporter.getAvailableTrucks().stream()
                .filter(t -> t.getTruckType().equalsIgnoreCase(requiredTruckType))
                .mapToInt(TransporterTruckCapacity::getCount)
                .findFirst()
                .orElse(0);

        if (request.trucksOffered() > availableCapacity) {
            throw new InsufficientCapacityException("Transporter does not have enough " + requiredTruckType + 
                                                    " trucks. Available: " + availableCapacity + 
                                                    ", Requested: " + request.trucksOffered());
        }

        // 4. Create and Save Bid
        Bid bid = new Bid();
        bid.setLoadId(request.loadId());
        bid.setTransporterId(request.transporterId());
        bid.setProposedRate(request.proposedRate());
        bid.setTrucksOffered(request.trucksOffered());
        bid.setStatus(BidStatus.PENDING);
        bid.setSubmittedAt(Timestamp.from(Instant.now()));

        Bid savedBid = bidRepository.save(bid);

        // 5. Update Load status to OPEN_FOR_BIDS if it was POSTED
        if (load.getStatus() == LoadStatus.POSTED) {
            load.setStatus(LoadStatus.OPEN_FOR_BIDS);
            loadRepository.save(load);
        }

        return mapEntityToResponse(savedBid);
    }

    /**
     * Rejects a specific bid.
     *
     * @param bidId The ID of the bid to reject.
     * @return The updated bid response.
     */
    @Transactional
    public BidResponse rejectBid(UUID bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + bidId));

        if (bid.getStatus() != BidStatus.PENDING) {
             throw new InvalidStatusTransitionException("Cannot reject a bid that is not PENDING.");
        }

        bid.setStatus(BidStatus.REJECTED);
        Bid savedBid = bidRepository.save(bid);

        return mapEntityToResponse(savedBid);
    }

    /**
     * Retrieves a bid by its ID.
     *
     * @param bidId The ID of the bid.
     * @return The bid response.
     */
    @Transactional(readOnly = true)
    public BidResponse getBidById(UUID bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found with id: " + bidId));
        return mapEntityToResponse(bid);
    }

    /**
     * Retrieves bids based on filters with pagination.
     *
     * @param loadId        Optional load ID filter.
     * @param transporterId Optional transporter ID filter.
     * @param status        Optional status filter.
     * @param pageable      Pagination information.
     * @return A page of bid responses.
     */
    @Transactional(readOnly = true)
    public Page<BidResponse> getBids(UUID loadId, UUID transporterId, BidStatus status, Pageable pageable) {
        return bidRepository.findBids(loadId, transporterId, status, pageable)
                .map(this::mapEntityToResponse);
    }

    private BidResponse mapEntityToResponse(Bid bid) {
        return new BidResponse(
                bid.getBidId(),
                bid.getLoadId(),
                bid.getTransporterId(),
                bid.getProposedRate(),
                bid.getTrucksOffered(),
                bid.getStatus(),
                bid.getSubmittedAt(),
                null // Score is not calculated here, only in LoadService.getBestBids
        );
    }
}
