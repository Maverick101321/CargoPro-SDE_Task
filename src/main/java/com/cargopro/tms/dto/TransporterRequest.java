package com.cargopro.tms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for registering a new Transporter.
 */
public record TransporterRequest(
    @NotBlank(message = "Company name is required")
    String companyName,

    @NotNull(message = "Rating is required")
    Double rating
) {}
