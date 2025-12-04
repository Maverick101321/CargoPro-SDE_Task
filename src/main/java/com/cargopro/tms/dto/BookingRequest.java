package com.cargopro.tms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * DTO for creating a Booking (accepting a bid).
 */
public record BookingRequest(
    @NotNull(message = "Bid ID is required")
    UUID bidId,

    @Min(value = 1, message = "Allocated trucks must be at least 1")
    int allocatedTrucks,

    @Positive(message = "Final rate must be positive")
    double finalRate
) {}
