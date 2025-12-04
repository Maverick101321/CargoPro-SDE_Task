package com.cargopro.tms.service;

import com.cargopro.tms.dto.TransporterRequest;
import com.cargopro.tms.entity.Transporter;
import com.cargopro.tms.entity.TransporterTruckCapacity;
import com.cargopro.tms.exception.ResourceNotFoundException;
import com.cargopro.tms.repository.TransporterRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing Transporters.
 * Annotated with @Service to indicate it's a Spring-managed bean holding business logic.
 */
@Service
public class TransporterService {

    private final TransporterRepository transporterRepository;

    public TransporterService(TransporterRepository transporterRepository) {
        this.transporterRepository = transporterRepository;
    }

    /**
     * Registers a new transporter.
     * Converts the DTO request to an entity and saves it.
     *
     * @param request The transporter registration request.
     * @return The saved Transporter entity.
     */
    @Transactional // Ensures the database operation is atomic.
    public Transporter registerTransporter(TransporterRequest request) {
        Transporter transporter = new Transporter();
        transporter.setCompanyName(request.companyName());
        transporter.setRating(request.rating());
        return transporterRepository.save(transporter);
    }

    /**
     * Updates the truck capacity for a specific transporter.
     * Fetches the transporter, updates the list of available trucks, and saves the changes.
     *
     * @param transporterId   The ID of the transporter.
     * @param capacityUpdates The list of new truck capacities.
     * @return The updated Transporter entity.
     */
    @Transactional // Ensures the update is committed as a single transaction.
    public Transporter updateTruckCapacity(UUID transporterId, List<TransporterTruckCapacity> capacityUpdates) {
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + transporterId));

        // Update the available trucks
        // Since it's a unidirectional OneToMany, we can replace the list.
        // Hibernate will handle the foreign key updates/inserts.
        transporter.setAvailableTrucks(capacityUpdates);

        return transporterRepository.save(transporter);
    }

    /**
     * Retrieves the details of a specific transporter.
     *
     * @param transporterId The ID of the transporter.
     * @return The Transporter entity.
     * @throws ResourceNotFoundException if the transporter is not found.
     */
    @Transactional(readOnly = true) // Optimization for read-only operations.
    public Transporter getTransporterDetails(UUID transporterId) {
        return transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException("Transporter not found with id: " + transporterId));
    }
}
