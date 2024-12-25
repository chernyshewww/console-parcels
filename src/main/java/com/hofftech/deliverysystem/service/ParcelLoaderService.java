package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.strategy.LoadingStrategy;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.enums.StrategyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ParcelLoaderService {

    private final StrategyHelper strategyHelper;

    public List<Truck> loadParcels(List<Parcel> parcels, StrategyType strategyType, int availableTrucks) {
        log.info("Loading parcels using strategy {}", strategyType);
        LoadingStrategy strategy = strategyHelper.getStrategy(strategyType);
        return strategy.loadParcels(parcels, availableTrucks);
    }
}