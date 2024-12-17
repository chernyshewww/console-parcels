package com.deliverysystem.strategy;

import com.deliverysystem.service.ParcelService;
import com.deliverysystem.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaximumCapacityStrategy implements LoadingStrategy {
    private static final Logger logger = LoggerFactory.getLogger(MaximumCapacityStrategy.class);
    public static final double HALF_PARCEL_SUPPORT = 2.0;

    @Override
    public List<TruckService> loadParcels(List<char[][]> parcels) {
        logger.info("Executing MaximumCapacityStrategy");
        List<TruckService> trucks = new ArrayList<>();

        parcels.sort((a, b) -> Integer.compare(b.length * b[0].length, a.length * a[0].length));
        logger.info("Parcels sorted by area in descending order");

        for (char[][] parcelData : parcels) {
            ParcelService parcel = new ParcelService(Arrays.stream(parcelData).map(String::new).toList());
            boolean placed = false;

            for (TruckService truck : trucks) {
                if (tryPlaceParcel(truck, parcel)) {
                    placed = true;
                    logger.info("Parcel placed in existing truck");
                    break;
                }
            }

            if (!placed) {
                TruckService newTruck = new TruckService();
                if (tryPlaceParcel(newTruck, parcel)) {
                    trucks.add(newTruck);
                    logger.info("Parcel placed in new truck");
                } else {
                    logger.error("Failed to place parcel even in an empty truck");
                }
            }
        }

        logger.info("Total trucks used: {}", trucks.size());
        return trucks;
    }

    private boolean tryPlaceParcel(TruckService truck, ParcelService parcel) {
        for (var row = TruckService.HEIGHT - parcel.getData().length; row >= 0; row--) {
            for (var col = 0; col <= TruckService.WIDTH - parcel.getData()[0].length; col++) {
                if (truck.canPlace(parcel, row, col) && isSupported(truck, parcel, row, col)) {
                    truck.place(parcel, row, col);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSupported(TruckService truck, ParcelService parcel, int row, int col) {
        var width = parcel.getData()[0].length;
        var requiredSupport = (int) Math.ceil(width / HALF_PARCEL_SUPPORT);
        var supportCount = 0;

        if (row == TruckService.HEIGHT - parcel.getData().length) {
            return true;
        }

        for (var i = 0; i < width; i++) {
            if (truck.getGrid()[row + parcel.getData().length][col + i] != ' ') {
                supportCount++;
            }
        }
        return supportCount >= requiredSupport;
    }
}