package com.cargopro.tms.repository;

import com.cargopro.tms.entity.Load;
import com.cargopro.tms.entity.LoadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for accessing Load data.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface LoadRepository extends JpaRepository<Load, UUID> {

    /**
     * Retrieves a list of loads posted by a specific shipper.
     *
     * @param shipperId The ID of the shipper.
     * @return A list of Load entities associated with the given shipperId.
     */
    List<Load> findByShipperId(String shipperId);

    /**
     * Finds loads by shipperId and status with pagination.
     *
     * @param shipperId The ID of the shipper.
     * @param status    The status of the load.
     * @param pageable  Pagination information.
     * @return A page of loads matching the criteria.
     */
    Page<Load> findByShipperIdAndStatus(String shipperId, LoadStatus status, Pageable pageable);
}
