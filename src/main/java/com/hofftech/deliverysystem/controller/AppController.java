package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.*;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.enums.StrategyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class AppController {

    private static final String FILE_NAME = "src/main/resources/parcels.txt";
    private final StrategyHelper strategyHelper;
    private final ParcelLoaderService loaderService;
    private final FileParserService fileParserService;
    private final JsonReaderService jsonReaderService;
    private final TextWriterService textWriterService;
    private final ResultWriterService resultWriterService;
    private final TruckService truckService;

    @Autowired
    public AppController(StrategyHelper strategyHelper,
                         ParcelLoaderService loaderService,
                         FileParserService fileParserService,
                         JsonReaderService jsonReaderService,
                         TextWriterService textWriterService,
                         ResultWriterService resultWriterService,
                         TruckService truckService) {
        this.strategyHelper = strategyHelper;
        this.loaderService = loaderService;
        this.fileParserService = fileParserService;
        this.jsonReaderService = jsonReaderService;
        this.textWriterService = textWriterService;
        this.resultWriterService = resultWriterService;
        this.truckService = truckService;
    }

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
            System.out.println("Choose mode:");
            System.out.println("1: Read from text file and print trucks");
            System.out.println("2: Read from JSON file and create a text file");
            Scanner scanner = new Scanner(System.in);
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
            System.out.println("Choose strategy:");
            System.out.println("1: Maximum Capacity Strategy");
            System.out.println("2: One to One Strategy");
            System.out.println("3: Equal Distribution Strategy");

            Scanner scanner = new Scanner(System.in);
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

            Scanner scanner = new Scanner(System.in);
            availableTrucks = scanner.nextInt();
            scanner.nextLine();

            if (availableTrucks < 1) {
                System.out.println("Please enter a valid number of trucks (greater than 0).");
            }
        } while (availableTrucks < 1);

        return availableTrucks;
    }

    private void executeJsonBasedLoading() throws RuntimeException{
        try {
            List<Truck> trucks = jsonReaderService.readTrucksFromJson("truck_state.json");
            textWriterService.writeTrucksToTextFromJson("truck_state.json", "output.txt");
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void processParcelsForStrategy(List<Parcel> parcels, StrategyType type, int availableTrucks) {
        System.out.println("Loading parcels using strategy: " + type);

        List<Truck> trucks = loaderService.loadParcels(parcels, type, availableTrucks);
        System.out.println("Number of trucks used for " + type + " strategy: " + trucks.size());

        resultWriterService.writeTrucksToJson(trucks, "truck_state.json");

        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("Printing truck #" + (i + 1) + " for strategy " + type);
            truckService.printTruck(trucks.get(i),i + 1);
        }
    }
}
