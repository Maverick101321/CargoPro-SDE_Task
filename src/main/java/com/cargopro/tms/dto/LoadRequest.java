package com.cargopro.tms.dto;

import com.cargopro.tms.entity.WeightUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.sql.Timestamp;

/**
 * DTO for creating a new Load.
 * Contains validation constraints to ensure data integrity.
 */
public record LoadRequest(
    @NotBlank(message = "Shipper ID is required")
    String shipperId,

    @NotBlank(message = "Loading city is required")
    String loadingCity,

    @NotBlank(message = "Unloading city is required")
    String unloadingCity,

    @NotNull(message = "Loading date is required")
    Timestamp loadingDate,

    @NotBlank(message = "Product type is required")
    String productType,

    @Positive(message = "Weight must be positive")
    double weight,

    @NotNull(message = "Weight unit is required")
    WeightUnit weightUnit,

    @NotBlank(message = "Truck type is required")
    String truckType,

    @Min(value = 1, message = "At least one truck is required")
    int numOfTrucks
) {}
