package com.cargopro.tms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * DTO for submitting a new Bid.
 */
public record BidRequest(
    @NotNull(message = "Load ID is required")
    UUID loadId,

    @NotNull(message = "Transporter ID is required")
    UUID transporterId,

    @Positive(message = "Proposed rate must be positive")
    double proposedRate,

    @Min(value = 1, message = "At least one truck must be offered")
    int trucksOffered
) {}
