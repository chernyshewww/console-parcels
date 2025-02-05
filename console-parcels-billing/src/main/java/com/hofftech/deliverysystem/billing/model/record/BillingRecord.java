package com.hofftech.deliverysystem.billing.model.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingRecord {

    private String user;
    private LocalDateTime timestamp;
    private String operationType;
    private int segments;
    private int parcels;
    private int cost;
}