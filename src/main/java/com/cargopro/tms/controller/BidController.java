package com.cargopro.tms.controller;

import com.cargopro.tms.dto.BidRequest;
import com.cargopro.tms.dto.BidResponse;
import com.cargopro.tms.entity.BidStatus;
import com.cargopro.tms.service.BidService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing Bids.
 * Base path: /bid
 */
@RestController
@RequestMapping("/bid")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    /**
     * 1. POST /bid
     * Submits a new bid for a load.
     * Validates capacity and load status via the service.
     *
     * @param request The bid submission details.
     * @return The created bid response with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<BidResponse> submitBid(@Valid @RequestBody BidRequest request) {
        BidResponse response = bidService.submitBid(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 2. GET /bid
     * Retrieves bids based on optional filters (loadId, transporterId, status) with pagination.
     *
     * @param loadId        Optional load ID filter.
     * @param transporterId Optional transporter ID filter.
     * @param status        Optional status filter.
     * @param pageable      Pagination information.
     * @return A page of bid responses with HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<Page<BidResponse>> getBids(
            @RequestParam(required = false) UUID loadId,
            @RequestParam(required = false) UUID transporterId,
            @RequestParam(required = false) BidStatus status,
            Pageable pageable) {
        Page<BidResponse> response = bidService.getBids(loadId, transporterId, status, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. GET /bid/{bidId}
     * Retrieves details of a specific bid.
     *
     * @param bidId The ID of the bid.
     * @return The bid details with HTTP 200 OK.
     */
    @GetMapping("/{bidId}")
    public ResponseEntity<BidResponse> getBidById(@PathVariable UUID bidId) {
        BidResponse response = bidService.getBidById(bidId);
        return ResponseEntity.ok(response);
    }

    /**
     * 4. PATCH /bid/{bidId}/reject
     * Rejects a specific bid.
     *
     * @param bidId The ID of the bid to reject.
     * @return The updated bid response with HTTP 200 OK.
     */
    @PatchMapping("/{bidId}/reject")
    public ResponseEntity<BidResponse> rejectBid(@PathVariable UUID bidId) {
        BidResponse response = bidService.rejectBid(bidId);
        return ResponseEntity.ok(response);
    }
}
