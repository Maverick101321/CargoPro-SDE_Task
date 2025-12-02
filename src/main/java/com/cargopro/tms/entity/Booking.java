package com.cargopro.tms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Entity class representing a confirmed Booking in the Transport Management System.
 * A booking is created when a bid is accepted for a load.
 */
@Data // Lombok annotation to automatically generate getters, setters, toString, equals, and hashCode methods.
@Entity // Specifies that this class is a JPA entity.
@Table(name = "bookings") // Optional: Specifies the name of the database table.
public class Booking {

    /**
     * Unique identifier for the Booking.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID bookingId;

    /**
     * The ID of the Load associated with this booking.
     * Acts as a foreign key to the Load entity.
     */
    @Column(nullable = false)
    private UUID loadId;

    /**
     * The ID of the accepted Bid that resulted in this booking.
     * Acts as a foreign key to the Bid entity.
     */
    @Column(nullable = false)
    private UUID bidId;

    /**
     * The ID of the Transporter who won the bid and is responsible for the load.
     * Acts as a foreign key to the Transporter entity.
     */
    @Column(nullable = false)
    private UUID transporterId;

    /**
     * The number of trucks allocated for this booking.
     * This should match the number of trucks offered in the accepted bid.
     */
    private int allocatedTrucks;

    /**
     * The final agreed rate for the booking.
     * This typically matches the proposed rate in the accepted bid.
     */
    private double finalRate;

    /**
     * The current status of the booking (e.g., CONFIRMED, COMPLETED).
     */
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    /**
     * The date and time when the booking was confirmed.
     */
    private Timestamp bookedAt;
}
