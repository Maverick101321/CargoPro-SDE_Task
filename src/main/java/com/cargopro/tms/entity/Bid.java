package com.cargopro.tms.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Entity class representing a Bid placed by a Transporter for a specific Load.
 */
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

    // Getters and Setters

    public UUID getBidId() {
        return bidId;
    }

    public void setBidId(UUID bidId) {
        this.bidId = bidId;
    }

    public UUID getLoadId() {
        return loadId;
    }

    public void setLoadId(UUID loadId) {
        this.loadId = loadId;
    }

    public UUID getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(UUID transporterId) {
        this.transporterId = transporterId;
    }

    public double getProposedRate() {
        return proposedRate;
    }

    public void setProposedRate(double proposedRate) {
        this.proposedRate = proposedRate;
    }

    public int getTrucksOffered() {
        return trucksOffered;
    }

    public void setTrucksOffered(int trucksOffered) {
        this.trucksOffered = trucksOffered;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }
}
