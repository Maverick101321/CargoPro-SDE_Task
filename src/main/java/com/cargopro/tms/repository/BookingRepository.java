package com.cargopro.tms.repository;

import com.cargopro.tms.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Repository interface for accessing Booking data.
 */
public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
