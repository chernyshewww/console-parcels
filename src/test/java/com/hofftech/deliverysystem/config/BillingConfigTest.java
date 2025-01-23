package com.hofftech.deliverysystem.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class BillingConfigTest {

    @Test
    @DisplayName("Должен установить и вернуть правильную цену за погрузку")
    void testSetAndGetLoadPricing() {
        BillingConfig billingConfig = new BillingConfig();

        billingConfig.setLoadPricing(100);

        assertThat(billingConfig.getLoadPricing()).isEqualTo(100);
    }

    @Test
    @DisplayName("Должен установить и вернуть правильную цену за разгрузку")
    void testSetAndGetUnloadPricing() {
        BillingConfig billingConfig = new BillingConfig();

        billingConfig.setUnloadPricing(200);

        assertThat(billingConfig.getUnloadPricing()).isEqualTo(200);
    }

    @Test
    @DisplayName("Должен установить и вернуть правильную цену за грузовик")
    void testSetAndGetTruckPricing() {
        BillingConfig billingConfig = new BillingConfig();

        billingConfig.setTruckPricing(300);

        assertThat(billingConfig.getTruckPricing()).isEqualTo(300);
    }

    @Test
    @DisplayName("Должен вернуть значения по умолчанию для всех цен")
    void testDefaultValues() {
        BillingConfig billingConfig = new BillingConfig();

        assertThat(billingConfig.getLoadPricing()).isZero();
        assertThat(billingConfig.getUnloadPricing()).isZero();
        assertThat(billingConfig.getTruckPricing()).isZero();
    }
}
