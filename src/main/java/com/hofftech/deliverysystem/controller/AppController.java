package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.FileParserService;
import com.hofftech.deliverysystem.service.ParcelLoaderService;
import com.hofftech.deliverysystem.service.ResultWriterService;
import com.hofftech.deliverysystem.service.TextWriterService;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.enums.StrategyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
@Slf4j
public class AppController {

    private static final String FILE_NAME = "src/main/resources/parcels.txt";
    private static final String OUTPUT_JSON_FILE_NAME = "truck_state.json";
    private static final String OUTPUT_TXT_FILE_NAME = "output.txt";

    private final StrategyHelper strategyHelper;
    private final ParcelLoaderService loaderService;
    private final FileParserService fileParserService;
    private final TextWriterService textWriterService;
    private final ResultWriterService resultWriterService;
    private final TruckService truckService;
    private final Scanner scanner;

    public void run() {
        int mode = getModeFromUser();
        switch (mode) {
            case 1:
                executeStrategyBasedLoading();
                break;
            case 2:
                executeJsonBasedLoading();
                break;
            default:
                throw new IllegalArgumentException("Unexpected mode: " + mode);
        }
    }

    private int getModeFromUser() {
        int mode;
        do {
            System.out.println("""
                        Choose mode:
                        1: Read from text file and print trucks
                        2: Read from JSON file and create a text file
                    """);

            mode = scanner.nextInt();
            scanner.nextLine();

            if (mode != 1 && mode != 2) {
                System.out.println("Invalid mode. Please choose either 1 or 2.");
            }
        } while (mode != 1 && mode != 2);

        return mode;
    }

    private void executeStrategyBasedLoading() {
        StrategyType selectedStrategy = getStrategyFromUser();
        int availableTrucks = getAvailableTrucksFromUser();

        List<Parcel> parcels = fileParserService.readParcelsFromFile(FILE_NAME);

        processParcelsForStrategy(parcels, selectedStrategy, availableTrucks);
    }

    private StrategyType getStrategyFromUser() {
        StrategyType selectedStrategy = null;
        while (selectedStrategy == null) {
            System.out.println("""
                        Choose strategy:
                        1: Maximum Capacity Strategy
                        2: One to One Strategy
                        3: Equal Distribution Strategy
                    """);

            selectedStrategy = strategyHelper.getStrategyByChoice(scanner.nextInt());
            scanner.nextLine();

            if (selectedStrategy == null) {
                System.out.println("Invalid strategy choice. Please try again.");
            }
        }
        return selectedStrategy;
    }

    private int getAvailableTrucksFromUser() {
        int availableTrucks;
        do {
            System.out.println("Enter the number of trucks:");

            availableTrucks = scanner.nextInt();
            scanner.nextLine();

            if (availableTrucks < 1) {
                System.out.println("Please enter a valid number of trucks (greater than 0).");
            }
        } while (availableTrucks < 1);

        return availableTrucks;
    }

    private void executeJsonBasedLoading() {
        textWriterService.writeTrucksToTextFromJson(OUTPUT_JSON_FILE_NAME, OUTPUT_TXT_FILE_NAME);
    }

    private void processParcelsForStrategy(List<Parcel> parcels, StrategyType type, int availableTrucks) {
        log.info("Loading parcels using strategy: {}", type);

        List<Truck> trucks = loaderService.loadParcels(parcels, type, availableTrucks);
        System.out.println("Number of trucks used for " + type + " strategy: " + trucks.size());

        resultWriterService.writeTrucksToJson(trucks, OUTPUT_JSON_FILE_NAME);

        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("Printing truck #" + (i + 1) + " for strategy " + type);
            truckService.printTruck(trucks.get(i), i + 1);
        }
    }
}
