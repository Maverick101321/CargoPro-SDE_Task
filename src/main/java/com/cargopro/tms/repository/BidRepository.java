package com.cargopro.tms.repository;

import com.cargopro.tms.entity.Bid;
import com.cargopro.tms.entity.BidStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

/**
 * Repository interface for accessing Bid data.
 */
public interface BidRepository extends JpaRepository<Bid, UUID> {

    /**
     * Finds bids based on optional loadId, transporterId, and status.
     *
     * @param loadId        Optional load ID.
     * @param transporterId Optional transporter ID.
     * @param status        Optional bid status.
     * @param pageable      Pagination information.
     * @return A page of bids matching the criteria.
     */
    @Query("SELECT b FROM Bid b WHERE " +
           "(:loadId IS NULL OR b.loadId = :loadId) AND " +
           "(:transporterId IS NULL OR b.transporterId = :transporterId) AND " +
           "(:status IS NULL OR b.status = :status)")
    Page<Bid> findBids(@Param("loadId") UUID loadId, 
                       @Param("transporterId") UUID transporterId, 
                       @Param("status") BidStatus status, 
                       Pageable pageable);

    /**
     * Finds all bids for a specific load.
     *
     * @param loadId The ID of the load.
     * @return A list of bids for the load.
     */
    java.util.List<Bid> findByLoadId(UUID loadId);
}
