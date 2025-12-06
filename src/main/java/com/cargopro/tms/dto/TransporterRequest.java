package com.cargopro.tms.dto;

import com.cargopro.tms.entity.TransporterTruckCapacity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for registering a new Transporter.
 */
public record TransporterRequest(
        @NotBlank(message = "Company name is required") String companyName,

        @NotNull(message = "Rating is required") Double rating,

        List<TransporterTruckCapacity> availableTrucks) {
}
