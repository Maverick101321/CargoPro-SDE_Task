package com.cargopro.tms.dto;

import com.cargopro.tms.entity.LoadStatus;
import com.cargopro.tms.entity.WeightUnit;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * DTO for returning Load details.
 */
public record LoadResponse(
    UUID loadId,
    String shipperId,
    String loadingCity,
    String unloadingCity,
    Timestamp loadingDate,
    String productType,
    double weight,
    WeightUnit weightUnit,
    String truckType,
    int numOfTrucks,
    LoadStatus status,
    Timestamp datePosted
) {}
