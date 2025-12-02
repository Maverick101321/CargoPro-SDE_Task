package com.cargopro.tms.service;

import com.cargopro.tms.dto.LoadRequest;
import com.cargopro.tms.dto.LoadResponse;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing Loads.
 */
public interface LoadService {

    /**
     * Creates a new load.
     *
     * @param loadRequest The load details.
     * @return The created load response.
     */
    LoadResponse createLoad(LoadRequest loadRequest);

    /**
     * Retrieves all loads for a specific shipper.
     *
     * @param shipperId The ID of the shipper.
     * @return A list of load responses.
     */
    List<LoadResponse> getLoadsByShipperId(String shipperId);

    /**
     * Retrieves a specific load by its ID.
     *
     * @param loadId The ID of the load.
     * @return The load response.
     */
    LoadResponse getLoadById(UUID loadId);

    /**
     * Updates an existing load.
     *
     * @param loadId The ID of the load to update.
     * @param loadRequest The new load details.
     * @return The updated load response.
     */
    LoadResponse updateLoad(UUID loadId, LoadRequest loadRequest);

    /**
     * Deletes a load by its ID.
     *
     * @param loadId The ID of the load to delete.
     */
    void deleteLoad(UUID loadId);
}
