package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.enums.StrategyType;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.ParcelFormatter;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.util.TruckGenerator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StrategyHelper {

    private final TruckGenerator truckGenerator;
    private final TruckService truckService;

    private static final double HALF_PARCEL_SUPPORT = 2.0;

    public LoadingStrategy getStrategy(StrategyType strategyType) {
        return switch (strategyType) {
            case MAXIMUM_CAPACITY -> new MaximumCapacityStrategy(this);
            case ONE_TO_ONE -> new OneParcelPerTruckStrategy(truckService);
            case EQUAL_DISTRIBUTION -> new EqualDistributionStrategy(this, truckGenerator);
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

    public boolean tryPlaceParcel(Truck truck, ParcelFormatter parcel) {
        for (var row = truck.getHeight() - parcel.getData().length; row >= 0; row--) {
            for (var col = 0; col <= truck.getWidth() - parcel.getData()[0].length; col++) {
                if (truckService.canPlace(parcel, truck,row, col) && isSupported(truck, parcel, row, col)) {
                    truckService.place(parcel,truck, row, col);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSupported(Truck truck, ParcelFormatter parcel, int row, int col) {
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
}
