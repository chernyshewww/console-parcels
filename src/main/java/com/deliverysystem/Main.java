package com.deliverysystem;

import com.deliverysystem.service.FileParserService;
import com.deliverysystem.service.ParcelLoaderService;
import com.deliverysystem.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// todo разбей гад класс на сервисы.
// DONE
public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static final String FILE_PATH = "src/main/resources/parcels.txt";

//    @SuppressWarnings("ALL") //todo Зачем?
    //todo убрать лишние комментарии
    // The word 'package' is reserved in java, so we use 'parcel' instead
    public static void main(String[] args) {
        logger.info("Program started.");

        // Read parcels
        var parcels = FileParserService.readParcelsFromFile(FILE_PATH);
        //todo слишком много инфо. Лучше сделай если хочешь уровня debug
        logger.info("Read {} valid parcels from file.", parcels.size());

        // Strategy 1: Largest First
        logger.info("Using Strategy 1 (Largest First)");
        //todo название должно быть информативной. Лучше сделай чтобы понили. Тут загружают машину под завязку
        //в другом: посылку на машину
        var trucksStrategy1 = ParcelLoaderService.loadParcels(parcels, 1);

        System.out.println("\nStrategy 1: Largest First");
        trucksStrategy1.forEach(TruckService::print);

        // Strategy 2: One-to-One
        // Very strange strategy, but still it works
        logger.info("Using Strategy 2 (One-to-one)");
        var trucksStrategy2 = ParcelLoaderService.loadParcels(parcels, 2);

        System.out.println("\nStrategy 2: One-to-One");
        trucksStrategy2.forEach(TruckService::print);

        logger.info("Program finished. Total trucks used: Strategy 1: {}, Strategy 2: {}",
                trucksStrategy1.size(), trucksStrategy2.size());
    }
}
