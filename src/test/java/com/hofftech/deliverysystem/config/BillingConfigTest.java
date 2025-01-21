package com.hofftech.deliverysystem.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BillingConfigTest {

    @Test
    void testSetAndGetLoadPricing() {
        BillingConfig billingConfig = new BillingConfig();

        billingConfig.setLoadPricing(100);

        assertThat(billingConfig.getLoadPricing()).isEqualTo(100);
    }

    @Test
    void testSetAndGetUnloadPricing() {
        BillingConfig billingConfig = new BillingConfig();

        billingConfig.setUnloadPricing(200);

        assertThat(billingConfig.getUnloadPricing()).isEqualTo(200);
    }

    @Test
    void testSetAndGetTruckPricing() {
        BillingConfig billingConfig = new BillingConfig();

        billingConfig.setTruckPricing(300);

        assertThat(billingConfig.getTruckPricing()).isEqualTo(300);
    }

    @Test
    void testDefaultValues() {
        BillingConfig billingConfig = new BillingConfig();

        assertThat(billingConfig.getLoadPricing()).isZero();
        assertThat(billingConfig.getUnloadPricing()).isZero();
        assertThat(billingConfig.getTruckPricing()).isZero();
    }
}
