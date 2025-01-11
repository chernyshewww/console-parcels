package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

/**
 * A strategy that places parcels into the available trucks by trying to maximize truck capacity usage.
 * Parcels are first sorted by their area (in descending order), and each parcel is placed in the first available truck.
 * If a parcel cannot be placed in any existing truck, an error is thrown.
 */
@RequiredArgsConstructor
@Slf4j
public class MaximumCapacityStrategy implements LoadingStrategy {

    private final ParcelService parcelService;

    /**
     * Loads parcels into trucks by attempting to place each parcel in the first available truck.
     * If a parcel cannot be placed in any truck, an exception is thrown.
     * Parcels are first sorted by their area (width * height) in descending order to try and maximize the capacity usage of trucks.
     *
     * @param parcels A list of parcels to be loaded into the trucks.
     * @param trucks A list of trucks where the parcels will be loaded.
     * @return The list of trucks after the parcels have been loaded.
     * @throws IllegalStateException If any parcel cannot be placed in the given trucks.
     */
    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, List<Truck> trucks) {
        log.info("Executing MaximumCapacityStrategy");

        parcels.sort(Comparator.comparingInt(parcel -> -parcel.getForm().length * parcel.getForm()[0].length));
        log.info("Parcels sorted by area in descending order");

        for (Parcel parcel : parcels) {
            boolean placed = false;

            for (Truck truck : trucks) {
                if (parcelService.tryPlaceParcel(truck, parcel)) {
                    placed = true;
                    log.info("Parcel placed in existing truck");
                    break;
                }
            }

            if (!placed) {
                String errorMessage = String.format("Unable to place parcel '%s' with dimensions [%dx%d] in the given trucks.",
                        parcel.getName(), parcel.getForm().length, parcel.getForm()[0].length);
                log.error(errorMessage);
                throw new IllegalStateException(errorMessage);
            }
        }

        log.info("All parcels successfully placed. Total trucks used: {}", trucks.size());
        return trucks;
    }
}
