package com.cargopro.tms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Entity class representing a Bid placed by a Transporter for a specific Load.
 */
@Data // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods.
@Entity // Specifies that this class is a JPA entity.
@Table(name = "bids") // Optional: Specifies the name of the database table.
public class Bid {

    /**
     * Unique identifier for the Bid.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID bidId;

    /**
     * The ID of the Load this bid is for.
     * This acts as a foreign key to the Load entity.
     * It is used for establishing the relationship and for indexing to quickly find bids for a load.
     */
    @Column(nullable = false)
    private UUID loadId;

    /**
     * The ID of the Transporter who submitted this bid.
     * This acts as a foreign key to the Transporter entity.
     * It is used for establishing the relationship and for indexing to quickly find bids by a transporter.
     */
    @Column(nullable = false)
    private UUID transporterId;

    /**
     * The rate proposed by the transporter for moving the load.
     */
    private double proposedRate;

    /**
     * The number of trucks the transporter is offering for this load.
     */
    private int trucksOffered;

    /**
     * The current status of the bid (e.g., PENDING, ACCEPTED).
     */
    @Enumerated(EnumType.STRING)
    private BidStatus status;

    /**
     * The date and time when the bid was submitted.
     */
    private Timestamp submittedAt;
}
