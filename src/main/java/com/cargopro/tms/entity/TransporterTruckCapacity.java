package com.cargopro.tms.entity;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Entity class representing the truck capacity of a transporter.
 * This class maps to a database table and is linked to the Transporter entity.
 */
@Entity // Specifies that this class is a JPA entity.
@Table(name = "transporter_truck_capacities") // Optional: Specifies the name of the database table.
public class TransporterTruckCapacity {

    /**
     * Unique identifier for the truck capacity record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    /**
     * The type of truck (e.g., "Flatbed", "Refrigerated").
     */
    private String truckType;

    /**
     * The number of trucks of this type available.
     */
    private int count;

    // Note: The relationship is typically managed from the parent (Transporter) side.
    // If bidirectional navigation is needed, a @ManyToOne field back to Transporter could be added here.

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
