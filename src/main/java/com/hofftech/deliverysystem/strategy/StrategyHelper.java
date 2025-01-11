package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.service.ParcelService;
import lombok.RequiredArgsConstructor;

/**
 * Helper class to determine the appropriate loading strategy based on the strategy type.
 * It creates instances of different loading strategies depending on the strategy type provided.
 */
@RequiredArgsConstructor
public class StrategyHelper {

    private final ParcelService parcelService;

    /**
     * Determines the appropriate loading strategy based on the strategy type.
     * The strategy type is provided as a string, and the method returns the corresponding
     * loading strategy.
     *
     * @param strategyType The type of loading strategy to be used.
     * @return The corresponding loading strategy, or null if the strategy type is unknown.
     */
    public LoadingStrategy determineStrategy(String strategyType) {
        return switch (strategyType) {
            case "Максимальная вместимость" -> new MaximumCapacityStrategy(parcelService);
            case "Одна машина - Одна посылка" -> new OneParcelPerTruckStrategy(parcelService);
            case "Равномерное распределение" -> new EqualDistributionStrategy(parcelService);
            default -> null;
        };
    }
}
