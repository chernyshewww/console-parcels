package com.deliverysystem;

import com.deliverysystem.service.*;
import com.deliverysystem.strategy.StrategyHelper;
import com.deliverysystem.enums.StrategyType;
import com.deliverysystem.model.Truck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String fileName  = "src/main/resources/parcels.txt";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            int mode = -1;
            while (mode != 1 && mode != 2) {
                System.out.println("Choose mode:");
                System.out.println("1: Read from text file and print trucks");
                System.out.println("2: Read from JSON file and create a text file");

                try {
                    mode = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter either 1 or 2.");
                    continue;
                }

                if (mode != 1 && mode != 2) {
                    System.out.println("Invalid mode. Please choose either 1 or 2.");
                }
            }

            switch (mode) {
                case 1:
                    StrategyType selectedStrategy = null;
                    StrategyHelper strategyHelper = new StrategyHelper();

                    while (selectedStrategy == null) {
                        System.out.println("Choose strategy:");
                        System.out.println("1: Maximum Capacity Strategy");
                        System.out.println("2: One to One Strategy");
                        System.out.println("3: Equal Distribution Strategy");

                        try {
                            selectedStrategy = strategyHelper.getStrategyByChoice(Integer.parseInt(scanner.nextLine()));

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter valid strategy.");
                        }
                    }

                    int availableTrucks = -1;
                    while (availableTrucks < 1) {
                        System.out.println("Also choose the number of trucks:");

                        try {
                            availableTrucks = Integer.parseInt(scanner.nextLine());

                            if (availableTrucks < 1) {
                                System.out.println("Please enter a valid number of trucks (greater than 0).");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter valid number.");
                        }
                    }


                    ParcelLoaderService loaderService = new ParcelLoaderService();
                    var parcels = FileParserService.readParcelsFromFile(fileName);

                    processParcelsForStrategy(loaderService, parcels, selectedStrategy, availableTrucks);
                    break;
                case 2:

                    List<Truck> trucks = JsonReaderService.readTrucksFromJson("truck_state.json");

                    TextWriterService.writeTrucksToTextFromJson("truck_state.json", "output.txt");
                    break;
            }


        } catch (Exception e) {
            logger.error("An error occurred in the application: {}", e.getMessage(), e);
        }
    }

    private static void processParcelsForStrategy(ParcelLoaderService loaderService,
                                                  List<char[][]> parcels,
                                                  StrategyType type,
                                                  int availableTrucks) throws IOException {
        logger.debug("Loading parcels using strategy: {}", type);

        var trucks = loaderService.loadParcels(parcels, type, availableTrucks);
        logger.debug("Number of trucks used for {} strategy: {}", type, trucks.size());
        ResultWriterService.writeTrucksToJson(trucks, "truck_state.json");

        for (int i = 0; i < trucks.size(); i++) {
            TruckService truckService = new TruckService(trucks.get(i));
            logger.debug("Printing truck #{} for strategy {}", i + 1, type);
            truckService.printTruck(i + 1);
        }
    }
}