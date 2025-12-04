package com.cargopro.tms.controller;

import com.cargopro.tms.dto.BookingRequest;
import com.cargopro.tms.dto.BookingResponse;
import com.cargopro.tms.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing Bookings.
 * Base path: /booking
 */
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * 1. POST /booking
     * Accepts a bid and creates a booking.
     * This is the main transactional endpoint that handles bid acceptance,
     * capacity allocation, and concurrency control.
     *
     * @param request The booking creation details (bidId, allocatedTrucks, finalRate).
     * @return The created booking response with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.acceptBidAndCreateBooking(
                request.bidId(),
                request.allocatedTrucks(),
                request.finalRate()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 2. GET /booking/{bookingId}
     * Retrieves details of a specific booking.
     *
     * @param bookingId The ID of the booking.
     * @return The booking details with HTTP 200 OK.
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable UUID bookingId) {
        BookingResponse response = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. PATCH /booking/{bookingId}/cancel
     * Cancels a booking.
     * Restores truck capacity to the transporter and updates the load status.
     *
     * @param bookingId The ID of the booking to cancel.
     * @return The updated booking response with HTTP 200 OK.
     */
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable UUID bookingId) {
        BookingResponse response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(response);
    }
}
