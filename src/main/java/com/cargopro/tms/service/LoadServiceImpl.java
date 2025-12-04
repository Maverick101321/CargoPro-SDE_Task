package com.cargopro.tms.service;

import com.cargopro.tms.dto.BidResponse;
import com.cargopro.tms.dto.LoadRequest;
import com.cargopro.tms.dto.LoadResponse;
import com.cargopro.tms.dto.LoadWithBidsResponse;
import com.cargopro.tms.entity.Bid;
import com.cargopro.tms.entity.Load;
import com.cargopro.tms.entity.LoadStatus;
import com.cargopro.tms.entity.Transporter;
import com.cargopro.tms.exception.InvalidStatusTransitionException;
import com.cargopro.tms.exception.ResourceNotFoundException;
import com.cargopro.tms.repository.BidRepository;
import com.cargopro.tms.repository.LoadRepository;
import com.cargopro.tms.repository.TransporterRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the LoadService interface.
 * Handles business logic for Load management, including creation, cancellation, and bid analysis.
 */
@Service
public class LoadServiceImpl implements LoadService {

    private final LoadRepository loadRepository;
    private final BidRepository bidRepository;
    private final TransporterRepository transporterRepository;

    public LoadServiceImpl(LoadRepository loadRepository, BidRepository bidRepository, TransporterRepository transporterRepository) {
        this.loadRepository = loadRepository;
        this.bidRepository = bidRepository;
        this.transporterRepository = transporterRepository;
    }

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
        Load load = findLoadByIdOrThrow(loadId);
        return mapEntityToResponse(load);
    }

    @Override
    @Transactional
    public LoadResponse updateLoad(UUID loadId, LoadRequest loadRequest) {
        Load load = findLoadByIdOrThrow(loadId);
        
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

    @Override
    @Transactional
    public void cancelLoad(UUID loadId) {
        Load load = findLoadByIdOrThrow(loadId);

        // Rule 2: Validate that the load is NOT currently BOOKED before canceling.
        if (load.getStatus() == LoadStatus.BOOKED) {
            throw new InvalidStatusTransitionException("Cannot cancel a load that is already BOOKED.");
        }

        load.setStatus(LoadStatus.CANCELLED);
        loadRepository.save(load);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoadResponse> getLoadsByFilter(String shipperId, LoadStatus status, Pageable pageable) {
        return loadRepository.findByShipperIdAndStatus(shipperId, status, pageable)
                .map(this::mapEntityToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LoadWithBidsResponse getLoadWithBids(UUID loadId) {
        Load load = findLoadByIdOrThrow(loadId);
        List<Bid> bids = bidRepository.findByLoadId(loadId);
        
        List<BidResponse> bidResponses = bids.stream()
                .map(bid -> mapBidToResponse(bid, null))
                .collect(Collectors.toList());

        return new LoadWithBidsResponse(mapEntityToResponse(load), bidResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BidResponse> getBestBids(UUID loadId) {
        // Fetch all active bids for the load
        List<Bid> bids = bidRepository.findByLoadId(loadId);

        return bids.stream()
                .map(bid -> {
                    // Fetch Transporter to get rating
                    Transporter transporter = transporterRepository.findById(bid.getTransporterId())
                            .orElseThrow(() -> new ResourceNotFoundException("Transporter not found for bid: " + bid.getBidId()));
                    
                    // Calculate Score
                    // Formula: Score = (1/proposedRate) * 0.7 + (rating/5) * 0.3
                    double rateScore = (1.0 / bid.getProposedRate()) * 0.7;
                    double ratingScore = (transporter.getRating() / 5.0) * 0.3;
                    double totalScore = rateScore + ratingScore;

                    return mapBidToResponse(bid, totalScore);
                })
                .sorted(Comparator.comparing(BidResponse::score).reversed()) // Sort by highest score
                .collect(Collectors.toList());
    }

    private Load findLoadByIdOrThrow(UUID loadId) {
        return loadRepository.findById(loadId)
                .orElseThrow(() -> new ResourceNotFoundException("Load not found with id: " + loadId));
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

    private BidResponse mapBidToResponse(Bid bid, Double score) {
        return new BidResponse(
                bid.getBidId(),
                bid.getLoadId(),
                bid.getTransporterId(),
                bid.getProposedRate(),
                bid.getTrucksOffered(),
                bid.getStatus(),
                bid.getSubmittedAt(),
                score
        );
    }
}
