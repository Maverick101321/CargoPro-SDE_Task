package com.cargopro.tms.repository;

import com.cargopro.tms.entity.Booking;
import com.cargopro.tms.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for accessing Booking data.
 */
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    /**
     * Finds bookings for a load with a specific status.
     *
     * @param loadId The ID of the load.
     * @param status The status of the booking.
     * @return A list of bookings.
     */
    List<Booking> findByLoadIdAndStatus(UUID loadId, BookingStatus status);
}
