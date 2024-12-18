package com.deliverysystem;

import com.deliverysystem.model.enums.StrategyType;
import com.deliverysystem.service.FileParserService;
import com.deliverysystem.service.ParcelLoaderService;
import com.deliverysystem.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String fileName  = "src/main/resources/parcels.txt";

    public static void main(String[] args) {
        try {
            ParcelLoaderService loaderService = new ParcelLoaderService();
            var parcels = FileParserService.readParcelsFromFile(fileName);

            processParcelsForStrategy(loaderService, parcels, StrategyType.MAXIMUM_CAPACITY);
            processParcelsForStrategy(loaderService, parcels, StrategyType.ONE_TO_ONE);

        } catch (Exception e) {
            logger.error("An error occurred in the application: {}", e.getMessage(), e);
        }
    }

    private static void processParcelsForStrategy(ParcelLoaderService loaderService, List<char[][]> parcels, StrategyType type) {
        logger.debug("Loading parcels using strategy: {}", type);

        var trucks = loaderService.loadParcels(parcels, type);
        logger.debug("Number of trucks used for {} strategy: {}", type, trucks.size());

        for (int i = 0; i < trucks.size(); i++) {
            TruckService truckService = new TruckService(trucks.get(i));
            logger.debug("Printing truck #{} for strategy {}", i + 1, type);
            truckService.printTruck(i + 1);
        }
    }
}