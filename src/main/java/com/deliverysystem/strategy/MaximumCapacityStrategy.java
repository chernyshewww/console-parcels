package com.deliverysystem.strategy;

import com.deliverysystem.model.Truck;
import com.deliverysystem.service.ParcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaximumCapacityStrategy implements LoadingStrategy {
    private static final Logger logger = LoggerFactory.getLogger(MaximumCapacityStrategy.class);

    @Override
    public List<Truck> loadParcels(List<char[][]> parcels, int availableTrucks) {
        StrategyHelper strategyHelper = new StrategyHelper();
        logger.info("Executing MaximumCapacityStrategy");
        List<Truck> trucks = new ArrayList<>();

        parcels.sort((a, b) -> Integer.compare(b.length * b[0].length, a.length * a[0].length));
        logger.info("Parcels sorted by area in descending order");

        for (char[][] parcelData : parcels) {
            ParcelService parcel = new ParcelService(Arrays.stream(parcelData).map(String::new).toList());
            boolean placed = false;

            for (Truck truck : trucks) {
                if (strategyHelper.tryPlaceParcel(truck, parcel)) {
                    placed = true;
                    logger.info("Parcel placed in existing truck");
                    break;
                }
            }

            if (!placed) {
                Truck truck = new Truck();
                if (strategyHelper.tryPlaceParcel(truck, parcel)) {
                    trucks.add(truck);
                } else {
                    logger.error("Failed to place parcel even in an empty truck");
                }
            }
        }

        if (trucks.size() > availableTrucks) {
            logger.error("Not enough trucks available to load all parcels");
            return new ArrayList<>();
        }

        logger.info("Total trucks used: {}", trucks.size());
        return trucks;
    }
}