package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.ParcelFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class MaximumCapacityStrategy implements LoadingStrategy {

    private final StrategyHelper strategyHelper;
    private final ParcelFormatter parcelFormatter;

    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, int availableTrucks) {
        log.info("Executing MaximumCapacityStrategy");
        List<Truck> trucks = new ArrayList<>();

        parcels.sort(Comparator.comparingInt(parcel -> -parcel.data().length * parcel.data()[0].length));
        log.info("Parcels sorted by area in descending order");

        for (Parcel parcelData : parcels) {
            Parcel parcel = parcelFormatter.convertToMatrix(Arrays.stream(parcelData.data()).map(String::new).toList());
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