package com.deliverysystem.strategy;

import com.deliverysystem.service.ParcelService;
import com.deliverysystem.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OneParcelPerTruckStrategy implements LoadingStrategy {
    private static final Logger logger = LoggerFactory.getLogger(OneParcelPerTruckStrategy.class);

    private boolean placeParcelFromBottom(TruckService truck, ParcelService parcel) {
        for (var row = TruckService.HEIGHT - parcel.getData().length; row >= 0; row--) {
            if (truck.canPlace(parcel, row, 0)) {
                truck.place(parcel, row, 0);
                return true;
            }
        }
        logger.warn("Could not place parcel in the truck");
        return false;
    }

    @Override
    public List<TruckService> loadParcels(List<char[][]> parcels) {
        logger.info("Executing OneParcelPerTruckStrategy");
        List<TruckService> trucks = new ArrayList<>();

        for (char[][] parcelData : parcels) {
            ParcelService parcel = new ParcelService(Arrays.stream(parcelData).map(String::new).toList());

            TruckService newTruck = new TruckService();
            if (placeParcelFromBottom(newTruck, parcel)) {
                trucks.add(newTruck);
                logger.debug("Placed parcel in a new truck");
            } else {
                logger.warn("Failed to place parcel in a new truck");
            }
        }

        logger.info("Total trucks used in OneParcelPerTruckStrategy: {}", trucks.size());
        return trucks;
    }
}