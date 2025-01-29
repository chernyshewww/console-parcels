package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.config.BillingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @InjectMocks
    private PricingService pricingService;

    @Mock
    private BillingConfig billingConfig;

    @Test
    @DisplayName("Должен правильно вычислять стоимость загрузки")
    void calculateLoadCost_ShouldReturnCorrectCost() {
        int trucksCount = 5;
        int parcelsCount = 10;
        int loadPricing = 50;
        int truckPricing = 100;

        Mockito.when(billingConfig.getLoadPricing()).thenReturn(loadPricing);
        Mockito.when(billingConfig.getTruckPricing()).thenReturn(truckPricing);

        int result = pricingService.calculateLoadCost(trucksCount, parcelsCount);

        assertThat(result).isEqualTo(5 * 100 + 10 * 50);
    }

    @Test
    @DisplayName("Должен правильно вычислять стоимость выгрузки")
    void calculateUnloadCost_ShouldReturnCorrectCost() {
        int trucksCount = 3;
        int parcelsCount = 8;
        int unloadPricing = 40;
        int truckPricing = 120;

        Mockito.when(billingConfig.getUnloadPricing()).thenReturn(unloadPricing);
        Mockito.when(billingConfig.getTruckPricing()).thenReturn(truckPricing);

        int result = pricingService.calculateUnloadCost(trucksCount, parcelsCount);

        assertThat(result).isEqualTo(3 * 120 + 8 * 40);
    }

    @Test
    @DisplayName("Должен правильно вычислять стоимость при нулевом числе грузовиков и посылок")
    void calculateLoadCost_ShouldHandleZeroTrucksAndParcels() {
        int trucksCount = 0;
        int parcelsCount = 0;
        int loadPricing = 50;
        int truckPricing = 100;

        Mockito.when(billingConfig.getLoadPricing()).thenReturn(loadPricing);
        Mockito.when(billingConfig.getTruckPricing()).thenReturn(truckPricing);

        int result = pricingService.calculateLoadCost(trucksCount, parcelsCount);

        assertThat(result).isZero();
    }

    @Test
    @DisplayName("Должен правильно вычислять стоимость выгрузки при нулевом числе грузовиков и посылок")
    void calculateUnloadCost_ShouldHandleZeroTrucksAndParcels() {
        int trucksCount = 0;
        int parcelsCount = 0;
        int unloadPricing = 40;
        int truckPricing = 120;

        Mockito.when(billingConfig.getUnloadPricing()).thenReturn(unloadPricing);
        Mockito.when(billingConfig.getTruckPricing()).thenReturn(truckPricing);

        int result = pricingService.calculateUnloadCost(trucksCount, parcelsCount);

        assertThat(result).isZero();
    }

    @Test
    @DisplayName("Должен правильно вычислять стоимость загрузки при отрицательных значениях")
    void calculateLoadCost_ShouldHandleNegativeValues() {
        int trucksCount = -1;
        int parcelsCount = -2;
        int loadPricing = 50;
        int truckPricing = 100;

        Mockito.when(billingConfig.getLoadPricing()).thenReturn(loadPricing);
        Mockito.when(billingConfig.getTruckPricing()).thenReturn(truckPricing);

        int result = pricingService.calculateLoadCost(trucksCount, parcelsCount);

        assertThat(result).isEqualTo(-1 * 100 + -2 * 50);
    }

    @Test
    @DisplayName("Должен правильно вычислять стоимость выгрузки при отрицательных значениях")
    void calculateUnloadCost_ShouldHandleNegativeValues() {
        int trucksCount = -3;
        int parcelsCount = -4;
        int unloadPricing = 40;
        int truckPricing = 120;

        Mockito.when(billingConfig.getUnloadPricing()).thenReturn(unloadPricing);
        Mockito.when(billingConfig.getTruckPricing()).thenReturn(truckPricing);

        int result = pricingService.calculateUnloadCost(trucksCount, parcelsCount);

        assertThat(result).isEqualTo(-3 * 120 + -4 * 40);
    }
}
