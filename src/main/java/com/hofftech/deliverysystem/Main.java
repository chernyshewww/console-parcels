package com.hofftech.deliverysystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofftech.deliverysystem.controller.AppController;
import com.hofftech.deliverysystem.service.*;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.util.TruckGenerator;

public class Main {

    public static void main(String[] args) {
        try {
            FileReaderService fileReaderService = new FileReaderService();
            FileParserService fileParserService = new FileParserService(fileReaderService);
            JsonReaderService jsonReaderService = new JsonReaderService();
            ObjectMapper objectMapper = new ObjectMapper();
            TextWriterService textWriterService = new TextWriterService(objectMapper);
            ResultWriterService resultWriterService = new ResultWriterService(objectMapper);
            TruckService truckService = new TruckService();
            TruckGenerator truckGenerator = new TruckGenerator();
            StrategyHelper strategyHelper = new StrategyHelper(truckGenerator, truckService);
            ParcelLoaderService loaderService = new ParcelLoaderService(strategyHelper);

            AppController appController = new AppController(strategyHelper, loaderService, fileParserService, jsonReaderService, textWriterService, resultWriterService, truckService);
            appController.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
