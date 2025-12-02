package com.cargopro.tms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Entity class representing a Load in the Transport Management System.
 * This class maps to a database table and includes optimistic locking support.
 */
@Data // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods.
@Entity // Specifies that this class is a JPA entity and maps to a database table.
@Table(name = "loads") // Optional: Specifies the name of the database table.
public class Load {

    /**
     * Unique identifier for the Load.
     * UUID is used to ensure global uniqueness.
     */
    @Id // Specifies the primary key of an entity.
    @GeneratedValue(strategy = GenerationType.AUTO) // Configures the way of increment of the specified column(field).
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID loadId;

    /**
     * Identifier of the shipper who posted the load.
     */
    private String shipperId;

    /**
     * The city where the load is to be picked up.
     */
    private String loadingCity;

    /**
     * The city where the load is to be delivered.
     */
    private String unloadingCity;

    /**
     * The date and time when the loading is scheduled.
     */
    private Timestamp loadingDate;

    /**
     * Type of product being transported.
     */
    private String productType;

    /**
     * Weight of the load.
     */
    private double weight;

    /**
     * Unit of measurement for the weight (e.g., KG, TON).
     * Stored as a String in the database for readability.
     */
    @Enumerated(EnumType.STRING) // Specifies that the enum should be persisted by name (e.g., "KG") rather than ordinal.
    private WeightUnit weightUnit;

    /**
     * Type of truck required for this load.
     */
    private String truckType;

    /**
     * Number of trucks required.
     */
    private int numOfTrucks;

    /**
     * Current status of the load (e.g., POSTED, BOOKED).
     * Stored as a String in the database.
     */
    @Enumerated(EnumType.STRING)
    private LoadStatus status;

    /**
     * The date and time when the load was posted.
     */
    private Timestamp datePosted;

    /**
     * Version field for Optimistic Locking.
     * 
     * Optimistic Locking Mechanism:
     * When a transaction reads this entity, it also reads the version number.
     * When the transaction attempts to update the entity, it checks if the version in the database
     * matches the version it read.
     * - If they match, the update proceeds and the version is incremented.
     * - If they do not match, it means another transaction has updated the record in the meantime.
     *   In this case, an OptimisticLockException is thrown, preventing lost updates.
     * 
     * This ensures data integrity without locking the database row for long periods.
     */
    @Version // Specifies the version field or property of an entity class that serves as its optimistic lock value.
    private Long version;
}
