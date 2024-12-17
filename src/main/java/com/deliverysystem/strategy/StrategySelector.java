package com.deliverysystem.strategy;

import com.deliverysystem.model.enums.StrategyType;

public class StrategySelector {

    public static LoadingStrategy getStrategy(StrategyType strategyType) {
        return switch (strategyType) {
            case MAXIMUM_CAPACITY -> new MaximumCapacityStrategy();
            case ONE_TO_ONE -> new OneParcelPerTruckStrategy();
            default -> throw new IllegalArgumentException("Invalid strategy type: " + strategyType);
        };
    }
}
