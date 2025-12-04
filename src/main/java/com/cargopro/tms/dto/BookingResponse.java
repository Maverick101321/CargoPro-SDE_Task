package com.cargopro.tms.dto;

import com.cargopro.tms.entity.BookingStatus;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * DTO for returning Booking details.
 */
public record BookingResponse(
    UUID bookingId,
    UUID loadId,
    UUID bidId,
    UUID transporterId,
    int allocatedTrucks,
    double finalRate,
    BookingStatus status,
    Timestamp bookedAt
) {}
