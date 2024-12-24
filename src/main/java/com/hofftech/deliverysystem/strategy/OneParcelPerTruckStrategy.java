package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.ParcelFormatter;
import com.hofftech.deliverysystem.service.TruckService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class OneParcelPerTruckStrategy implements LoadingStrategy {

    private final TruckService truckService;

    public OneParcelPerTruckStrategy(TruckService truckService) {
        this.truckService = new TruckService();
    }

    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, int availableTrucks) {
        log.info("Executing OneParcelPerTruckStrategy");
        List<Truck> trucks = new ArrayList<>();

        for (Parcel parcel : parcels) {
            ParcelFormatter parcelFormatter = new ParcelFormatter(Arrays.stream(parcel.getData()).map(String::new).toList());

            Truck newTruck = new Truck();
            if (placeParcelFromBottom(newTruck, parcelFormatter)) {
                trucks.add(newTruck);
            } else {
                log.warn("Failed to place parcel in a new truck");
            }
        }

        if (trucks.size() > availableTrucks) {
            log.error("Not enough trucks available to load all parcels");
            return new ArrayList<>();
        }

        log.info("Total trucks used in OneParcelPerTruckStrategy: {}", trucks.size());
        return trucks;
    }

    private boolean placeParcelFromBottom(Truck truck, ParcelFormatter parcel) {
        for (var row = truck.getHeight() - parcel.getData().length; row >= 0; row--) {
            if (truckService.canPlace(parcel, truck, row, 0)) {
                truckService.place(parcel, truck, row, 0);
                return true;
            }
        }
        log.warn("Could not place parcel in the truck");
        return false;
    }
}