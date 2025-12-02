package com.cargopro.tms.repository;

import com.cargopro.tms.entity.Load;
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
}
