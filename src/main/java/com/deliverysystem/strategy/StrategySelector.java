package com.deliverysystem.strategy;

import com.deliverysystem.enums.StrategyType;

public class StrategySelector {

    public static LoadingStrategy getStrategy(StrategyType strategyType) {
        return switch (strategyType) {
            case MAXIMUM_CAPACITY -> new MaximumCapacityStrategy();
            case ONE_TO_ONE -> new OneParcelPerTruckStrategy();
        };
    }
}
