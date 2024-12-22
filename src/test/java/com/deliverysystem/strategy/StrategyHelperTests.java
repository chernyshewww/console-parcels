package com.deliverysystem.strategy;

import static org.assertj.core.api.Assertions.*;

import com.deliverysystem.enums.StrategyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StrategyHelperTests {

    private StrategyHelper strategyHelper;

    @BeforeEach
    void setUp() {
        strategyHelper = new StrategyHelper();
    }

    @Test
    void testGetStrategy_MaximumCapacity() {
        StrategyType strategyType = StrategyType.MAXIMUM_CAPACITY;

        var strategy = strategyHelper.getStrategy(strategyType);

        assertThat(strategy).isInstanceOf(MaximumCapacityStrategy.class);
    }

    @Test
    void testGetStrategy_OneToOne() {
        StrategyType strategyType = StrategyType.ONE_TO_ONE;

        var strategy = strategyHelper.getStrategy(strategyType);

        assertThat(strategy).isInstanceOf(OneParcelPerTruckStrategy.class);
    }

    @Test
    void testGetStrategy_EqualDistribution() {
        StrategyType strategyType = StrategyType.EQUAL_DISTRIBUTION;

        var strategy = strategyHelper.getStrategy(strategyType);

        assertThat(strategy).isInstanceOf(EqualDistributionStrategy.class);
    }

    @Test
    void testGetStrategyByChoice_1() {
        int choice = 1;

        StrategyType strategyType = strategyHelper.getStrategyByChoice(choice);

        assertThat(strategyType).isEqualTo(StrategyType.MAXIMUM_CAPACITY);
    }

    @Test
    void testGetStrategyByChoice_2() {
        int choice = 2;

        StrategyType strategyType = strategyHelper.getStrategyByChoice(choice);

        assertThat(strategyType).isEqualTo(StrategyType.ONE_TO_ONE);
    }

    @Test
    void testGetStrategyByChoice_3() {
        int choice = 3;

        StrategyType strategyType = strategyHelper.getStrategyByChoice(choice);

        assertThat(strategyType).isEqualTo(StrategyType.EQUAL_DISTRIBUTION);
    }

    @Test
    void testGetStrategyByChoice_InvalidChoice() {
        int choice = 99;

        StrategyType strategyType = strategyHelper.getStrategyByChoice(choice);

        assertThat(strategyType).isNull();
    }
}
