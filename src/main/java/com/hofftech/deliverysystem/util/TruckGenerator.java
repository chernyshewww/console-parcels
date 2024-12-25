package com.hofftech.deliverysystem.util;

import com.hofftech.deliverysystem.model.Truck;

import java.util.ArrayList;
import java.util.List;

public class TruckGenerator {

    public List<Truck> generateTrucks(int truckCount) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < truckCount; i++) {
            trucks.add(new Truck());
        }
        return trucks;
    }
}
