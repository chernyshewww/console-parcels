package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.enums.StrategyType;
import com.hofftech.deliverysystem.model.Parcel;
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
            case MAXIMUM_CAPACITY -> new MaximumCapacityStrategy(this, new ParcelFormatter());
            case ONE_TO_ONE -> new OneParcelPerTruckStrategy(truckService, new ParcelFormatter());
            case EQUAL_DISTRIBUTION -> new EqualDistributionStrategy(this, truckGenerator, new ParcelFormatter());
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

    public boolean tryPlaceParcel(Truck truck, Parcel parcel) {
        for (var row = truck.getHeight() - parcel.data().length; row >= 0; row--) {
            for (var col = 0; col <= truck.getWidth() - parcel.data()[0].length; col++) {
                if (truckService.canPlace(parcel, truck,row, col) && isSupported(truck, parcel, row, col)) {
                    truckService.place(parcel,truck, row, col);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSupported(Truck truck, Parcel parcel, int row, int col) {
        var width = parcel.data()[0].length;
        var requiredSupport = (int) Math.ceil(width / HALF_PARCEL_SUPPORT);
        var supportCount = 0;

        if (row == truck.getHeight() - parcel.data().length) {
            return true;
        }

        for (var i = 0; i < width; i++) {
            if (truck.getGrid()[row + parcel.data().length][col + i] != ' ') {
                supportCount++;
            }
        }
        return supportCount >= requiredSupport;
    }
}
