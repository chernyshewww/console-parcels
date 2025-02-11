package com.hofftech.deliverysystem.billing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "billing")
public class BillingConfig {

    private int loadPricing;
    private int unloadPricing;
    private int truckPricing;
}
