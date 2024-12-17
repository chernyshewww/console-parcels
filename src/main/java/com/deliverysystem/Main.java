package com.deliverysystem;

import com.deliverysystem.model.enums.StrategyType;
import com.deliverysystem.service.FileParserService;
import com.deliverysystem.service.ParcelLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Program started");

        try {
            ParcelLoaderService loaderService = new ParcelLoaderService();

            String fileName = "src/main/resources/parcels.txt";
            logger.debug("Reading parcels from file: {}", fileName);
            var parcels = FileParserService.readParcelsFromFile(fileName);
            logger.debug("Number of parcels loaded: {}", parcels.size());

            StrategyType type = StrategyType.MAXIMUM_CAPACITY;
            logger.debug("Loading parcels using strategy: {}", type);
            var trucks = loaderService.loadParcels(parcels, type);
            logger.debug("Number of trucks used for {} strategy: {}", type, trucks.size());

            for (int i = 0; i < trucks.size(); i++) {
                logger.debug("Printing truck #{} for strategy {}", i + 1, type);
                trucks.get(i).printTruck(i + 1);
            }

            type = StrategyType.ONE_TO_ONE;
            logger.debug("Loading parcels using strategy: {}", type);
            trucks = loaderService.loadParcels(parcels, type);
            logger.debug("Number of trucks used for {} strategy: {}", type, trucks.size());

            for (int i = 0; i < trucks.size(); i++) {
                logger.debug("Printing truck #{} for strategy {}", i + 1, type);
                trucks.get(i).printTruck(i + 1);
            }
        } catch (Exception e) {
            logger.error("An error occurred in the application: {}", e.getMessage(), e);
        }

        logger.debug("Program finished");
    }
}