package com.hofftech.deliverysystem.model.record.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Billing{

    private String user;
    private LocalDateTime timestamp;
    private String operationType;
    private int segments;
    private int parcels;
    private int cost;
}