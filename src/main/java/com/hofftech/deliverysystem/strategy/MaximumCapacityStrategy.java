package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.ParcelFormatter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class MaximumCapacityStrategy implements LoadingStrategy {

    private final StrategyHelper strategyHelper;

    public MaximumCapacityStrategy(StrategyHelper strategyHelper){
        this.strategyHelper = strategyHelper;
    }

    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, int availableTrucks) {
        log.info("Executing MaximumCapacityStrategy");
        List<Truck> trucks = new ArrayList<>();

        parcels.sort(Comparator.comparingInt(parcel -> -parcel.getData().length * parcel.getData()[0].length));
        log.info("Parcels sorted by area in descending order");

        for (Parcel parcelData : parcels) {
            ParcelFormatter parcel = new ParcelFormatter(Arrays.stream(parcelData.getData()).map(String::new).toList());
            boolean placed = false;

            for (Truck truck : trucks) {
                if (strategyHelper.tryPlaceParcel(truck, parcel)) {
                    placed = true;
                    log.info("Parcel placed in existing truck");
                    break;
                }
            }

            if (!placed) {
                Truck truck = new Truck();
                if (strategyHelper.tryPlaceParcel(truck, parcel)) {
                    trucks.add(truck);
                } else {
                    log.error("Failed to place parcel even in an empty truck");
                }
            }
        }

        if (trucks.size() > availableTrucks) {
            log.error("Not enough trucks available to load all parcels");
            return new ArrayList<>();
        }

        log.info("Total trucks used: {}", trucks.size());
        return trucks;
    }
}