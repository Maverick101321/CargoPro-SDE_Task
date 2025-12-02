package com.cargopro.tms.repository;

import com.cargopro.tms.entity.Transporter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * Repository interface for accessing Transporter data.
 */
public interface TransporterRepository extends JpaRepository<Transporter, UUID> {
}
