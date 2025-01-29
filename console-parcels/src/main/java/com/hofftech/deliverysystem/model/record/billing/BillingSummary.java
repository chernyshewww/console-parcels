package com.hofftech.deliverysystem.model.record.billing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BillingSummary {

    private LocalDate timestamp;
    private String operationType;
    private int segments;
    private int parcels;
    private int cost;
}