package com.hofftech.deliverysystem.billing.service;

import com.hofftech.deliverysystem.billing.config.BillingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for calculating costs for loading and unloading operations.
 * <p>
 * This service uses pricing configurations defined in {@link BillingConfig}
 * to calculate the costs based on the number of trucks and parcels involved.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PricingService {

    private final BillingConfig billingConfig;

    /**
     * Calculates the cost of a loading operation.
     *
     * @param trucksCount  The number of trucks involved in the operation.
     * @param parcelsCount The number of parcels loaded.
     * @return The total cost of the loading operation.
     */
    public int calculateLoadCost(int trucksCount, int parcelsCount) {
        return billingConfig.getLoadPricing() * parcelsCount + billingConfig.getTruckPricing() * trucksCount;
    }

    /**
     * Calculates the cost of an unloading operation.
     *
     * @param trucksCount  The number of trucks involved in the operation.
     * @param parcelsCount The number of parcels unloaded.
     * @return The total cost of the unloading operation.
     */
    public int calculateUnloadCost(int trucksCount, int parcelsCount) {
        return billingConfig.getUnloadPricing() * parcelsCount + billingConfig.getTruckPricing() * trucksCount;
    }
}
