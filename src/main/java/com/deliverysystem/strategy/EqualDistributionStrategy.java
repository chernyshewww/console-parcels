package com.deliverysystem.strategy;

import com.deliverysystem.model.Truck;
import com.deliverysystem.service.ParcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EqualDistributionStrategy implements LoadingStrategy {
    private static final Logger logger = LoggerFactory.getLogger(EqualDistributionStrategy.class);

    @Override
    public List<Truck> loadParcels(List<char[][]> parcels, int availableTrucks) {
        logger.info("Executing EqualDistributionStrategy");

        parcels.sort(Comparator.comparingInt(parcel -> -parcel.length * parcel[0].length));
        logger.info("Parcels sorted by area in descending order");

        List<Truck> trucks = initializeTrucks(availableTrucks);

        distributeParcelsAcrossTrucks(parcels, trucks);

        logger.info("Total trucks used: {}", trucks.size());
        return trucks;
    }

    private List<Truck> initializeTrucks(int truckCount) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < truckCount; i++) {
            trucks.add(new Truck());
        }
        return trucks;
    }

    private void distributeParcelsAcrossTrucks(List<char[][]> parcels, List<Truck> trucks) {
        int truckIndex = 0;
        int totalTrucks = trucks.size();

        for (char[][] parcelData : parcels) {
            ParcelService parcel = createParcelFromData(parcelData);

            boolean placed = tryPlaceParcelInTruck(trucks, truckIndex, parcel);

            if (!placed) {
                truckIndex = (truckIndex + 1) % totalTrucks;
                placed = tryPlaceParcelInTruck(trucks, truckIndex, parcel);
            }

            if (!placed) {
                logger.error("Failed to place parcel in any truck");
            }

            truckIndex = (truckIndex + 1) % totalTrucks;
        }
    }

    private ParcelService createParcelFromData(char[][] parcelData) {
        return new ParcelService(Arrays.stream(parcelData).map(String::new).toList());
    }

    private boolean tryPlaceParcelInTruck(List<Truck> trucks, int truckIndex, ParcelService parcel) {
        StrategyHelper strategyHelper = new StrategyHelper();
        Truck truck = trucks.get(truckIndex);
        boolean placed = strategyHelper.tryPlaceParcel(truck, parcel);

        if (placed) {
            logger.info("Parcel placed in truck {}", truckIndex + 1);
        }

        return placed;
    }
}
