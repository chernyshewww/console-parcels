package com.hofftech.deliverysystem.util;

import com.hofftech.deliverysystem.model.Truck;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TruckGenerator {

    public List<Truck> generateTrucks(int truckCount) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < truckCount; i++) {
            trucks.add(new Truck());
        }
        return trucks;
    }
}
