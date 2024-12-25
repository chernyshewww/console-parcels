package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.ParcelFormatter;
import com.hofftech.deliverysystem.util.TruckGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class EqualDistributionStrategy implements LoadingStrategy {

    private final StrategyHelper strategyHelper;
    private final TruckGenerator truckGenerator;
    private final ParcelFormatter parcelFormatter;

    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, int availableTrucks) {
        log.info("Executing EqualDistributionStrategy");

        parcels.sort(Comparator.comparingInt(parcel -> -parcel.data().length * parcel.data()[0].length));
        log.info("Parcels sorted by area in descending order");

        List<Truck> trucks = truckGenerator.generateTrucks(availableTrucks);

        distributeParcelsAcrossTrucks(parcels, trucks);

        log.info("Total trucks used: {}", trucks.size());
        return trucks;
    }

    private void distributeParcelsAcrossTrucks(List<Parcel> parcels, List<Truck> trucks) {
        int truckIndex = 0;
        int totalTrucks = trucks.size();

        for (Parcel parcelData : parcels) {
            Parcel parcel = parcelFormatter.convertToMatrix(Arrays.stream(parcelData.data()).map(String::new).toList());

            boolean placed = tryPlaceParcelInTruck(trucks, truckIndex, parcel);

            if (!placed) {
                log.error("Failed to place parcel in any truck");
            }

            truckIndex = (truckIndex + 1) % totalTrucks;
        }
    }

    private boolean tryPlaceParcelInTruck(List<Truck> trucks, int truckIndex, Parcel parcel) {
        Truck truck = trucks.get(truckIndex);
        boolean placed = strategyHelper.tryPlaceParcel(truck, parcel);

        if (placed) {
            log.info("Parcel placed in truck {}", truckIndex + 1);
        }

        return placed;
    }
}
