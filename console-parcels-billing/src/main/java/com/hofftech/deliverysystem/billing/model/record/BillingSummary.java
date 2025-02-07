package com.hofftech.deliverysystem.billing.model.record;

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

    @Override
    public String toString() {
        return String.format(
                "Date: %s, Operation: %s, Segments: %d, Parcels: %d, Cost: %d",
                timestamp, operationType, segments, parcels, cost
        );
    }
}