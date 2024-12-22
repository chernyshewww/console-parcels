package com.deliverysystem.strategy;

import com.deliverysystem.enums.StrategyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StrategyHelperTests {

    private StrategyHelper strategyHelper;

    @BeforeEach
    void setUp() {
        strategyHelper = new StrategyHelper();
    }

    @Test
    void getStrategy_GivenMaximumCapacityStrategyType_ReturnsMaximumCapacityStrategy() {
        StrategyType strategyType = StrategyType.MAXIMUM_CAPACITY;

        var strategy = strategyHelper.getStrategy(strategyType);

        assertThat(strategy).isInstanceOf(MaximumCapacityStrategy.class);
    }

    @Test
    void getStrategy_GivenOneToOneStrategyType_ReturnsOneParcelPerTruckStrategy() {
        StrategyType strategyType = StrategyType.ONE_TO_ONE;

        var strategy = strategyHelper.getStrategy(strategyType);

        assertThat(strategy).isInstanceOf(OneParcelPerTruckStrategy.class);
    }

    @Test
    void getStrategy_GivenEqualDistributionStrategyType_ReturnsEqualDistributionStrategy() {
        StrategyType strategyType = StrategyType.EQUAL_DISTRIBUTION;

        var strategy = strategyHelper.getStrategy(strategyType);

        assertThat(strategy).isInstanceOf(EqualDistributionStrategy.class);
    }

    @Test
    void getStrategyByChoice_GivenChoice1_ReturnsMaximumCapacityStrategyType() {
        int choice = 1;

        StrategyType strategyType = strategyHelper.getStrategyByChoice(choice);

        assertThat(strategyType).isEqualTo(StrategyType.MAXIMUM_CAPACITY);
    }

    @Test
    void getStrategyByChoice_GivenChoice2_ReturnsOneToOneStrategyType() {
        int choice = 2;

        StrategyType strategyType = strategyHelper.getStrategyByChoice(choice);

        assertThat(strategyType).isEqualTo(StrategyType.ONE_TO_ONE);
    }

    @Test
    void getStrategyByChoice_GivenChoice3_ReturnsEqualDistributionStrategyType() {
        int choice = 3;

        StrategyType strategyType = strategyHelper.getStrategyByChoice(choice);

        assertThat(strategyType).isEqualTo(StrategyType.EQUAL_DISTRIBUTION);
    }

    @Test
    void getStrategyByChoice_GivenInvalidChoice_ReturnsNull() {
        int choice = 99;

        StrategyType strategyType = strategyHelper.getStrategyByChoice(choice);

        assertThat(strategyType).isNull();
    }
}
