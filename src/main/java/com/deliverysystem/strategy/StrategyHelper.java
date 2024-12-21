package com.deliverysystem.strategy;

import com.deliverysystem.enums.StrategyType;
import com.deliverysystem.model.Truck;
import com.deliverysystem.service.ParcelService;
import com.deliverysystem.service.TruckService;

public class StrategyHelper {
    public final double HALF_PARCEL_SUPPORT = 2.0;

    public LoadingStrategy getStrategy(StrategyType strategyType) {
        return switch (strategyType) {
            case MAXIMUM_CAPACITY -> new MaximumCapacityStrategy();
            case ONE_TO_ONE -> new OneParcelPerTruckStrategy();
            case EQUAL_DISTRIBUTION -> new EqualDistributionStrategy();
        };
    }

    public StrategyType getStrategyByChoice(int choice) {
        return switch (choice) {
            case 1 -> StrategyType.MAXIMUM_CAPACITY;
            case 2 -> StrategyType.ONE_TO_ONE;
            case 3 -> StrategyType.EQUAL_DISTRIBUTION;
            default -> null;
        };
    }

    public boolean isSupported(Truck truck, ParcelService parcel, int row, int col) {
        var width = parcel.getData()[0].length;
        var requiredSupport = (int) Math.ceil(width / HALF_PARCEL_SUPPORT);
        var supportCount = 0;

        if (row == truck.getHeight() - parcel.getData().length) {
            return true;
        }

        for (var i = 0; i < width; i++) {
            if (truck.getGrid()[row + parcel.getData().length][col + i] != ' ') {
                supportCount++;
            }
        }
        return supportCount >= requiredSupport;
    }

    public boolean tryPlaceParcel(Truck truck, ParcelService parcel) {
        TruckService truckService = new TruckService(truck);
        for (var row = truck.getHeight() - parcel.getData().length; row >= 0; row--) {
            for (var col = 0; col <= truck.getWidth() - parcel.getData()[0].length; col++) {
                if (truckService.canPlace(parcel, row, col) && isSupported(truck, parcel, row, col)) {
                    truckService.place(parcel, row, col);
                    return true;
                }
            }
        }
        return false;
    }
}
