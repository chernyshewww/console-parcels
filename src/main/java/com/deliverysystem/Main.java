package com.deliverysystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static final String FILE_PATH = "src/main/resources/parcels.txt";

    @SuppressWarnings("ALL")
    // The word 'package' is reserved in java, so we use 'parcel' instead
    public static void main(String[] args) {
        logger.info("Program started.");

        // Read parcels
        var parcels = FileParser.readParcelsFromFile(FILE_PATH);
        logger.info("Read {} valid parcels from file.", parcels.size());

        // Strategy 1: Largest First
        logger.info("Using Strategy 1 (Largest First)");
        var trucksStrategy1 = ParcelLoader.loadParcels(parcels, 1);

        System.out.println("\nStrategy 1: Largest First");
        trucksStrategy1.forEach(Truck::print);

        // Strategy 2: One-to-One
        // Very strange strategy, but still it works
        logger.info("Using Strategy 2 (One-to-one)");
        var trucksStrategy2 = ParcelLoader.loadParcels(parcels, 2);

        System.out.println("\nStrategy 2: One-to-One");
        trucksStrategy2.forEach(Truck::print);

        logger.info("Program finished. Total trucks used: Strategy 1: {}, Strategy 2: {}",
                trucksStrategy1.size(), trucksStrategy2.size());
    }
}