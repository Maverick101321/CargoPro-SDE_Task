package com.cargopro.tms.dto;

import com.cargopro.tms.entity.BidStatus;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * DTO for returning Bid details, including an optional calculated score.
 */
public record BidResponse(
    UUID bidId,
    UUID loadId,
    UUID transporterId,
    double proposedRate,
    int trucksOffered,
    BidStatus status,
    Timestamp submittedAt,
    Double score // Calculated score for best bid selection
) {}
