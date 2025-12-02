package com.cargopro.tms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a Transporter in the Transport Management System.
 */
@Data // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods.
@Entity // Specifies that this class is a JPA entity.
@Table(name = "transporters") // Optional: Specifies the name of the database table.
public class Transporter {

    /**
     * Unique identifier for the Transporter.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID transporterId;

    /**
     * Name of the transporter company.
     */
    private String companyName;

    /**
     * Rating of the transporter (1-5).
     */
    private double rating;

    /**
     * List of available truck capacities for this transporter.
     * Modeled as a One-to-Many relationship.
     * 
     * CascadeType.ALL: Operations on Transporter (persist, remove, etc.) propagate to TransporterTruckCapacity.
     * FetchType.LAZY: Capacities are loaded on demand.
     * @JoinColumn: Creates a foreign key column 'transporter_id' in the 'transporter_truck_capacities' table.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transporter_id") 
    private List<TransporterTruckCapacity> availableTrucks;
}
