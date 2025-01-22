package com.hofftech.deliverysystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "billing")
public class BillingConfig {

    private int loadPricing;
    private int unloadPricing;
    private int truckPricing;
    private String filePath;
}
