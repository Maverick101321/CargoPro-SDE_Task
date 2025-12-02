package com.cargopro.tms.service;

import com.cargopro.tms.dto.BidResponse;
import com.cargopro.tms.dto.LoadRequest;
import com.cargopro.tms.dto.LoadResponse;
import com.cargopro.tms.dto.LoadWithBidsResponse;
import com.cargopro.tms.entity.LoadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Cancels a load if it is not already booked.
     *
     * @param loadId The ID of the load to cancel.
     */
    void cancelLoad(UUID loadId);

    /**
     * Retrieves loads based on shipperId and status with pagination.
     *
     * @param shipperId The ID of the shipper.
     * @param status    The status of the load.
     * @param pageable  Pagination information.
     * @return A page of load responses.
     */
    Page<LoadResponse> getLoadsByFilter(String shipperId, LoadStatus status, Pageable pageable);

    /**
     * Retrieves a load and its associated active bids.
     *
     * @param loadId The ID of the load.
     * @return The load with bids response.
     */
    LoadWithBidsResponse getLoadWithBids(UUID loadId);

    /**
     * Calculates and retrieves the best bids for a load based on a scoring formula.
     *
     * @param loadId The ID of the load.
     * @return A list of bid responses sorted by score.
     */
    List<BidResponse> getBestBids(UUID loadId);
}
