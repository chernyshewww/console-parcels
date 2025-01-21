package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.service.ParcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class StrategyHelperTest {

    @Mock
    private ParcelService parcelService;

    @InjectMocks
    private StrategyHelper strategyHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDetermineStrategy_MaximumCapacity() {
        String strategyType = "Максимальная вместимость";

        LoadingStrategy strategy = strategyHelper.determineStrategy(strategyType);

        assertThat(strategy).isInstanceOf(MaximumCapacityStrategy.class);
    }

    @Test
    void testDetermineStrategy_OneParcelPerTruck() {
        String strategyType = "Одна машина - Одна посылка";

        LoadingStrategy strategy = strategyHelper.determineStrategy(strategyType);

        assertThat(strategy).isInstanceOf(OneParcelPerTruckStrategy.class);
    }

    @Test
    void testDetermineStrategy_EqualDistribution() {
        String strategyType = "Равномерное распределение";

        LoadingStrategy strategy = strategyHelper.determineStrategy(strategyType);

        assertThat(strategy).isInstanceOf(EqualDistributionStrategy.class);
    }

    @Test
    void testDetermineStrategy_UnknownStrategy() {
        String strategyType = "Unknown Strategy";

        LoadingStrategy strategy = strategyHelper.determineStrategy(strategyType);

        assertThat(strategy).isNull();
    }
}
