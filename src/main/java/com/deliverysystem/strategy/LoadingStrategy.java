package com.deliverysystem.strategy;

import com.deliverysystem.model.Truck;

import java.util.List;

public interface LoadingStrategy {
    List<Truck> loadParcels(List<char[][]> parcels);
}