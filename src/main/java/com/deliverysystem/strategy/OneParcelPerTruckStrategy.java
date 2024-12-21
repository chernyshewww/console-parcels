package com.deliverysystem.strategy;

import com.deliverysystem.model.Truck;
import com.deliverysystem.service.ParcelService;
import com.deliverysystem.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OneParcelPerTruckStrategy implements LoadingStrategy {
    private static final Logger logger = LoggerFactory.getLogger(OneParcelPerTruckStrategy.class);

    @Override
    public List<Truck> loadParcels(List<char[][]> parcels, int availableTrucks) {
        logger.info("Executing OneParcelPerTruckStrategy");
        List<Truck> trucks = new ArrayList<>();

        for (char[][] parcelData : parcels) {
            ParcelService parcel = new ParcelService(Arrays.stream(parcelData).map(String::new).toList());

            Truck newTruck = new Truck();
            if (placeParcelFromBottom(newTruck, parcel)) {
                trucks.add(newTruck);
            } else {
                logger.warn("Failed to place parcel in a new truck");
            }
        }

        if (trucks.size() > availableTrucks) {
            logger.error("Not enough trucks available to load all parcels");
            return new ArrayList<>();
        }

        logger.info("Total trucks used in OneParcelPerTruckStrategy: {}", trucks.size());
        return trucks;
    }

    private boolean placeParcelFromBottom(Truck truck, ParcelService parcel) {
        TruckService truckService = new TruckService(truck);
        for (var row = truck.getHeight() - parcel.getData().length; row >= 0; row--) {
            if (truckService.canPlace(parcel, row, 0)) {
                truckService.place(parcel, row, 0);
                return true;
            }
        }
        logger.warn("Could not place parcel in the truck");
        return false;
    }
}