package com.cargopro.tms.service;

import com.cargopro.tms.dto.LoadRequest;
import com.cargopro.tms.dto.LoadResponse;
import com.cargopro.tms.entity.Load;
import com.cargopro.tms.entity.LoadStatus;
import com.cargopro.tms.exception.ResourceNotFoundException;
import com.cargopro.tms.repository.LoadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the LoadService interface.
 */
@Service
@RequiredArgsConstructor
public class LoadServiceImpl implements LoadService {

    private final LoadRepository loadRepository;

    @Override
    @Transactional
    public LoadResponse createLoad(LoadRequest loadRequest) {
        Load load = new Load();
        mapRequestToEntity(loadRequest, load);
        
        // Set default values
        load.setStatus(LoadStatus.POSTED);
        load.setDatePosted(Timestamp.from(Instant.now()));

        Load savedLoad = loadRepository.save(load);
        return mapEntityToResponse(savedLoad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoadResponse> getLoadsByShipperId(String shipperId) {
        return loadRepository.findByShipperId(shipperId).stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LoadResponse getLoadById(UUID loadId) {
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));
        return mapEntityToResponse(load);
    }

    @Override
    @Transactional
    public LoadResponse updateLoad(UUID loadId, LoadRequest loadRequest) {
        Load load = loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));
        
        mapRequestToEntity(loadRequest, load);
        
        Load updatedLoad = loadRepository.save(load);
        return mapEntityToResponse(updatedLoad);
    }

    @Override
    @Transactional
    public void deleteLoad(UUID loadId) {
        if (!loadRepository.existsById(loadId)) {
            throw new ResourceNotFoundException("Load not found with id: " + loadId);
        }
        loadRepository.deleteById(loadId);
    }

    private void mapRequestToEntity(LoadRequest request, Load load) {
        load.setShipperId(request.shipperId());
        load.setLoadingCity(request.loadingCity());
        load.setUnloadingCity(request.unloadingCity());
        load.setLoadingDate(request.loadingDate());
        load.setProductType(request.productType());
        load.setWeight(request.weight());
        load.setWeightUnit(request.weightUnit());
        load.setTruckType(request.truckType());
        load.setNumOfTrucks(request.numOfTrucks());
    }

    private LoadResponse mapEntityToResponse(Load load) {
        return new LoadResponse(
                load.getLoadId(),
                load.getShipperId(),
                load.getLoadingCity(),
                load.getUnloadingCity(),
                load.getLoadingDate(),
                load.getProductType(),
                load.getWeight(),
                load.getWeightUnit(),
                load.getTruckType(),
                load.getNumOfTrucks(),
                load.getStatus(),
                load.getDatePosted()
        );
    }
}
