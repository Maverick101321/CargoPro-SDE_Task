package com.cargopro.tms.controller;

import com.cargopro.tms.dto.BidResponse;
import com.cargopro.tms.dto.LoadRequest;
import com.cargopro.tms.dto.LoadResponse;
import com.cargopro.tms.dto.LoadWithBidsResponse;
import com.cargopro.tms.entity.LoadStatus;
import com.cargopro.tms.service.LoadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing Loads.
 * Base path: /load
 */
@RestController
@RequestMapping("/load")
@RequiredArgsConstructor
public class LoadController {

    private final LoadService loadService;

    /**
     * 1. POST /load
     * Creates a new load.
     *
     * @param loadRequest The load details.
     * @return The created load response with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<LoadResponse> createLoad(@Valid @RequestBody LoadRequest loadRequest) {
        LoadResponse response = loadService.createLoad(loadRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 2. GET /load
     * Retrieves loads based on optional filters (shipperId, status) with pagination.
     *
     * @param shipperId Optional shipper ID filter.
     * @param status    Optional status filter.
     * @param pageable  Pagination information.
     * @return A page of load responses with HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<Page<LoadResponse>> getLoads(
            @RequestParam(required = false) String shipperId,
            @RequestParam(required = false) LoadStatus status,
            Pageable pageable) {
        Page<LoadResponse> response = loadService.getLoadsByFilter(shipperId, status, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. GET /load/{loadId}
     * Retrieves a specific load by its ID, including active bids.
     *
     * @param loadId The ID of the load.
     * @return The load details with active bids and HTTP 200 OK.
     */
    @GetMapping("/{loadId}")
    public ResponseEntity<LoadWithBidsResponse> getLoadById(@PathVariable UUID loadId) {
        LoadWithBidsResponse response = loadService.getLoadWithBids(loadId);
        return ResponseEntity.ok(response);
    }

    /**
     * 4. PATCH /load/{loadId}/cancel
     * Cancels a load.
     *
     * @param loadId The ID of the load to cancel.
     * @return HTTP 200 OK if successful.
     */
    @PatchMapping("/{loadId}/cancel")
    public ResponseEntity<Void> cancelLoad(@PathVariable UUID loadId) {
        loadService.cancelLoad(loadId);
        return ResponseEntity.ok().build();
    }

    /**
     * 5. GET /load/{loadId}/best-bids
     * Retrieves the best bids for a load, sorted by score.
     *
     * @param loadId The ID of the load.
     * @return A list of sorted bid responses with HTTP 200 OK.
     */
    @GetMapping("/{loadId}/best-bids")
    public ResponseEntity<List<BidResponse>> getBestBids(@PathVariable UUID loadId) {
        List<BidResponse> response = loadService.getBestBids(loadId);
        return ResponseEntity.ok(response);
    }
}
