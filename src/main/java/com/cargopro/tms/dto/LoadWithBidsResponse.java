package com.cargopro.tms.dto;

import java.util.List;

/**
 * DTO for returning a Load along with its active bids.
 */
public record LoadWithBidsResponse(
    LoadResponse load,
    List<BidResponse> bids
) {}
