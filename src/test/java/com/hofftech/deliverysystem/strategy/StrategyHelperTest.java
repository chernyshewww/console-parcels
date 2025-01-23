package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.service.ParcelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StrategyHelperTest {

    @Mock
    private ParcelService parcelService;

    @InjectMocks
    private StrategyHelper strategyHelper;

    @Test
    @DisplayName("Должен вернуть стратегию максимальной вместимости")
    void testDetermineStrategy_MaximumCapacity() {
        String strategyType = "Максимальная вместимость";

        LoadingStrategy strategy = strategyHelper.determineStrategy(strategyType);

        assertThat(strategy).isInstanceOf(MaximumCapacityStrategy.class);
    }

    @Test
    @DisplayName("Должен вернуть стратегию одна машина - одна посылка")
    void testDetermineStrategy_OneParcelPerTruck() {
        String strategyType = "Одна машина - Одна посылка";

        LoadingStrategy strategy = strategyHelper.determineStrategy(strategyType);

        assertThat(strategy).isInstanceOf(OneParcelPerTruckStrategy.class);
    }

    @Test
    @DisplayName("Должен вернуть стратегию равномерного распределения")
    void testDetermineStrategy_EqualDistribution() {
        String strategyType = "Равномерное распределение";

        LoadingStrategy strategy = strategyHelper.determineStrategy(strategyType);

        assertThat(strategy).isInstanceOf(EqualDistributionStrategy.class);
    }

    @Test
    @DisplayName("Должен вернуть null для неизвестной стратегии")
    void testDetermineStrategy_UnknownStrategy() {
        String strategyType = "Unknown Strategy";

        LoadingStrategy strategy = strategyHelper.determineStrategy(strategyType);

        assertThat(strategy).isNull();
    }
}
