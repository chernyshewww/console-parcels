package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;

import java.util.List;

public interface LoadingStrategy {

    List<Truck> loadParcels(List<Parcel> parcels, int availableTrucks);
}