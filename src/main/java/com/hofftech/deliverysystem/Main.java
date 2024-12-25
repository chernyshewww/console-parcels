package com.hofftech.deliverysystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofftech.deliverysystem.controller.AppController;
import com.hofftech.deliverysystem.service.FileParserService;
import com.hofftech.deliverysystem.service.FileReaderService;
import com.hofftech.deliverysystem.service.ParcelLoaderService;
import com.hofftech.deliverysystem.service.ResultWriterService;
import com.hofftech.deliverysystem.service.TextWriterService;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.util.TruckGenerator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        StrategyHelper strategyHelper = new StrategyHelper(new TruckGenerator(), new TruckService());
        ParcelLoaderService loaderService = new ParcelLoaderService(strategyHelper);
        FileParserService fileParserService = new FileParserService(new FileReaderService());
        ObjectMapper objectMapper = new ObjectMapper();
        TextWriterService textWriterService = new TextWriterService(objectMapper);
        ResultWriterService resultWriterService = new ResultWriterService(objectMapper);
        TruckService truckService = new TruckService();
        Scanner scanner = new Scanner(System.in);

        AppController appController = new AppController(strategyHelper,
                loaderService,
                fileParserService,
                textWriterService,
                resultWriterService,
                truckService,
                scanner);

        appController.run();
    }
}
