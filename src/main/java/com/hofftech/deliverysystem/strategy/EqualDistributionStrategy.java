package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

/**
 * A strategy that distributes parcels equally across trucks based on parcel size.
 * The parcels are first sorted by their area (in descending order) and then placed in trucks in a round-robin fashion.
 */
@RequiredArgsConstructor
@Slf4j
public class EqualDistributionStrategy implements LoadingStrategy {

    private final ParcelService parcelService;

    /**
     * Loads parcels into trucks by distributing them equally.
     * The parcels are first sorted by their area (in descending order) before being placed into trucks in a round-robin manner.
     *
     * @param parcels A list of parcels to be loaded into the trucks.
     * @param trucks A list of trucks where the parcels will be loaded.
     * @return The list of trucks after the parcels have been loaded.
     */
    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, List<Truck> trucks) {
        log.info("Executing EqualDistributionStrategy");

        parcels.sort(Comparator.comparingInt(parcel -> -parcel.getForm().length * parcel.getForm()[0].length));
        log.info("Parcels sorted by area in descending order");

        distributeParcelsAcrossTrucks(parcels, trucks);

        log.info("Total trucks used: {}", trucks.size());
        return trucks;
    }

    /**
     * Distributes the parcels across the trucks in a round-robin fashion.
     * Each parcel is attempted to be placed in a truck in a cyclic manner, starting from the first truck.
     *
     * @param parcels A list of parcels to be distributed.
     * @param trucks A list of trucks where the parcels will be distributed.
     */
    private void distributeParcelsAcrossTrucks(List<Parcel> parcels, List<Truck> trucks) {
        int truckIndex = 0;
        int totalTrucks = trucks.size();

        for (Parcel parcel : parcels) {
            boolean placed = tryPlaceParcelInTruck(trucks, truckIndex, parcel);

            if (!placed) {
                log.error("Failed to place parcel in any truck");
            }

            truckIndex = (truckIndex + 1) % totalTrucks;
        }
    }

    /**
     * Attempts to place a parcel into a specific truck.
     *
     * @param trucks A list of trucks to place the parcel in.
     * @param truckIndex The index of the truck where the parcel should be placed.
     * @param parcel The parcel to be placed in the truck.
     * @return true if the parcel was successfully placed, false otherwise.
     */
    private boolean tryPlaceParcelInTruck(List<Truck> trucks, int truckIndex, Parcel parcel) {
        Truck truck = trucks.get(truckIndex);
        boolean placed = parcelService.tryPlaceParcel(truck, parcel);

        if (placed) {
            log.info("Parcel placed in truck {}", truckIndex + 1);
        }

        return placed;
    }
}
