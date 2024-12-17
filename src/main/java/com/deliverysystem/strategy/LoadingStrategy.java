package com.deliverysystem.strategy;

import com.deliverysystem.service.TruckService;

import java.util.List;

public interface LoadingStrategy {
    List<TruckService> loadParcels(List<char[][]> parcels);
}