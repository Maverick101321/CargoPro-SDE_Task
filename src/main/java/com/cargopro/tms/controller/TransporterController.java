package com.cargopro.tms.controller;

import com.cargopro.tms.dto.TransporterRequest;
import com.cargopro.tms.entity.Transporter;
import com.cargopro.tms.entity.TransporterTruckCapacity;
import com.cargopro.tms.service.TransporterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing Transporters.
 * Base path: /transporter
 */
@RestController
@RequestMapping("/transporter")
@RequiredArgsConstructor
public class TransporterController {

    private final TransporterService transporterService;

    /**
     * 1. POST /transporter
     * Registers a new transporter.
     *
     * @param request The transporter registration details.
     * @return The registered transporter entity with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<Transporter> registerTransporter(@Valid @RequestBody TransporterRequest request) {
        Transporter transporter = transporterService.registerTransporter(request);
        return new ResponseEntity<>(transporter, HttpStatus.CREATED);
    }

    /**
     * 2. GET /transporter/{transporterId}
     * Retrieves details of a specific transporter.
     *
     * @param transporterId The ID of the transporter.
     * @return The transporter details with HTTP 200 OK.
     */
    @GetMapping("/{transporterId}")
    public ResponseEntity<Transporter> getTransporter(@PathVariable UUID transporterId) {
        Transporter transporter = transporterService.getTransporterDetails(transporterId);
        return ResponseEntity.ok(transporter);
    }

    /**
     * 3. PUT /transporter/{transporterId}/trucks
     * Updates the truck capacity for a specific transporter.
     *
     * @param transporterId The ID of the transporter.
     * @param capacityUpdates The list of truck capacities to update.
     * @return The updated transporter entity with HTTP 200 OK.
     */
    @PutMapping("/{transporterId}/trucks")
    public ResponseEntity<Transporter> updateTruckCapacity(
            @PathVariable UUID transporterId,
            @RequestBody List<TransporterTruckCapacity> capacityUpdates) {
        Transporter updatedTransporter = transporterService.updateTruckCapacity(transporterId, capacityUpdates);
        return ResponseEntity.ok(updatedTransporter);
    }
}
