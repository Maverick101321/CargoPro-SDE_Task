package com.cargopro.tms.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Entity class representing a Load in the Transport Management System.
 * This class maps to a database table and includes optimistic locking support.
 */
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
     */
    @Version // Specifies the version field or property of an entity class that serves as its optimistic lock value.
    private Long version;

    // Getters and Setters

    public UUID getLoadId() {
        return loadId;
    }

    public void setLoadId(UUID loadId) {
        this.loadId = loadId;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;
    }

    public String getLoadingCity() {
        return loadingCity;
    }

    public void setLoadingCity(String loadingCity) {
        this.loadingCity = loadingCity;
    }

    public String getUnloadingCity() {
        return unloadingCity;
    }

    public void setUnloadingCity(String unloadingCity) {
        this.unloadingCity = unloadingCity;
    }

    public Timestamp getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(Timestamp loadingDate) {
        this.loadingDate = loadingDate;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

    public int getNumOfTrucks() {
        return numOfTrucks;
    }

    public void setNumOfTrucks(int numOfTrucks) {
        this.numOfTrucks = numOfTrucks;
    }

    public LoadStatus getStatus() {
        return status;
    }

    public void setStatus(LoadStatus status) {
        this.status = status;
    }

    public Timestamp getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Timestamp datePosted) {
        this.datePosted = datePosted;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
