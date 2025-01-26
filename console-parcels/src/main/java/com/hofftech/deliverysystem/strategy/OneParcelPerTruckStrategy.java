package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

/**
 * A strategy that places each parcel into its own truck.
 * Parcels are sorted by area (in descending order) and then placed into the first available truck.
 * Each truck can only carry one parcel at a time, and an exception is thrown if a parcel cannot be placed.
 */
@RequiredArgsConstructor
@Slf4j
public class OneParcelPerTruckStrategy implements LoadingStrategy {

    private final ParcelService parcelService;

    /**
     * Loads parcels into trucks such that each truck will carry exactly one parcel.
     * Parcels are first sorted by their area (width * height) in descending order.
     * If a parcel cannot be placed in any truck, an exception is thrown.
     *
     * @param parcels A list of parcels to be loaded into the trucks.
     * @param trucks A list of trucks where each truck will carry exactly one parcel.
     * @return The list of trucks with the parcels loaded.
     * @throws IllegalStateException If any parcel cannot be placed in any truck.
     */
    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, List<Truck> trucks) {
        log.info("Executing OneParcelPerTruckStrategy");

        parcels.sort(Comparator.comparingInt(parcel -> -parcel.getForm().length * parcel.getForm()[0].length));
        log.info("Parcels sorted by area in descending order");

        boolean[] truckOccupied = new boolean[trucks.size()];

        for (Parcel parcel : parcels) {
            boolean placed = false;

            for (int i = 0; i < trucks.size(); i++) {
                if (!truckOccupied[i] && parcelService.tryPlaceParcel(trucks.get(i), parcel)) {
                    truckOccupied[i] = true;
                    placed = true;
                    log.info("Parcel '{}' successfully placed in truck {}", parcel.getName(), i);
                    break;
                }
            }

            if (!placed) {
                log.error("Failed to place parcel '{}' in any truck.", parcel.getName());
                throw new IllegalStateException("Не удалось разместить все посылки в предоставленные грузовики.");
            }
        }

        log.info("All parcels successfully placed. Total trucks used: {}", trucks.size());
        return trucks;
    }
}
