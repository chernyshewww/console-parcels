package com.deliverysystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParcelLoaderService {
    private static final Logger logger = LoggerFactory.getLogger(ParcelLoaderService.class);

    public static final int STRATEGY_LARGEST_FIRST = 1;
    public static final int STRATEGY_ONE_PARCEL_PER_TRUCK = 2;
    public static final char EMPTY_CELL = ' ';
    public static final double HALF_PARCEL_SUPPORT = 2.0;

    public static List<TruckService> loadParcels(List<char[][]> parcels, int strategy) {
        logger.info("Loading parcels using strategy {}", strategy);
        List<TruckService> trucks = new ArrayList<>();

        if (strategy == STRATEGY_LARGEST_FIRST) {
            // We need to sort them because of '7'. They are actually a big problem
            parcels.sort((a, b) -> Integer.compare(b.length * b[0].length, a.length * a[0].length));
            logger.info("Parcels sorted by area in descending order");

            for (char[][] parcelData : parcels) {
                ParcelService parcel = new ParcelService(Arrays.stream(parcelData).map(String::new).toList());
                var placed = false;

                for (TruckService truck : trucks) {
                    if (placeParcelFromBottom(truck, parcel)) {
                        placed = true;
                        logger.info("Parcel placed in existing truck");
                        break;
                    }
                }

                // Creating a new truck
                if (!placed) {
                    TruckService newTruckService = new TruckService();
                    placeParcelFromBottom(newTruckService, parcel);
                    trucks.add(newTruckService);
                    logger.info("New truck created for parcel");
                }
            }
        } else if (strategy == STRATEGY_ONE_PARCEL_PER_TRUCK) {
            for (char[][] parcelData : parcels) {
                ParcelService parcel = new ParcelService(Arrays.stream(parcelData).map(String::new).toList());
                TruckService newTruckService = new TruckService();
                placeParcelFromBottom(newTruckService, parcel);
                trucks.add(newTruckService);
                logger.info("Placed parcel in new truck (Strategy 2)");
            }
        }

        logger.info("Total trucks used: {}", trucks.size());
        return trucks;
    }

    // Still hate this algorithm
    // We need to place them from bottom
    // Still some issues with '7's
    private static boolean placeParcelFromBottom(TruckService truck, ParcelService parcel) {
        logger.debug("Trying to place parcel from bottom.");
        for (int row = TruckService.HEIGHT - parcel.getData().length; row >= 0; row--) {
            for (int col = 0; col <= TruckService.WIDTH - parcel.getData()[0].length; col++) {
                if (truck.canPlace(parcel, row, col) && hasValidSupport(truck, parcel, row, col)) {
                    truck.place(parcel, row, col);
                    logger.info("Parcel placed at row={}, col={}", row, col);
                    return true;
                }
            }
        }
        logger.warn("Parcel could not be placed in the truck");
        return false;
    }

    // I've decided to add a support check because it was not obvious in the first iteration of the program
    private static boolean hasValidSupport(TruckService truck, ParcelService parcel, int row, int col) {
        var parcelWidth = parcel.getData()[0].length;

        if (row == TruckService.HEIGHT - parcel.getData().length) {
            logger.debug("Parcel has floor support at row={}, col={}", row, col);
            return true;
        }

        var supportedCells = 0;
        for (var j = 0; j < parcelWidth; j++) {
            if (parcel.getData()[0][j] != EMPTY_CELL) {
                var belowRow = row + parcel.getData().length;
                if (belowRow < TruckService.HEIGHT && truck.getCell(belowRow, col + j) != EMPTY_CELL) {
                    supportedCells++;
                }
            }
        }

        // Parcel needs a support of at least half of its width
        var hasSupport = supportedCells >= (parcelWidth / HALF_PARCEL_SUPPORT);
        logger.debug("Parcel support check: {} supported cells out of {}", supportedCells, parcelWidth);
        return hasSupport;
    }
}