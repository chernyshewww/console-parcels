package com.deliverysystem.service;

import com.deliverysystem.model.Truck;
import com.deliverysystem.strategy.LoadingStrategy;
import com.deliverysystem.strategy.StrategySelector;
import com.deliverysystem.enums.StrategyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ParcelLoaderService {
    private static final Logger logger = LoggerFactory.getLogger(ParcelLoaderService.class);

    public List<Truck> loadParcels(List<char[][]> parcels, StrategyType strategyType) {
        logger.info("Loading parcels using strategy {}", strategyType);
        LoadingStrategy strategy = StrategySelector.getStrategy(strategyType);
        return strategy.loadParcels(parcels);
    }
}