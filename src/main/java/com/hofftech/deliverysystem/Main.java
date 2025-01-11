package com.hofftech.deliverysystem;

import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.FileService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.telegram.DeliveryBot;
import com.hofftech.deliverysystem.util.FormHelper;
import com.hofftech.deliverysystem.util.OutputHelper;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (input == null) {
                    throw new RuntimeException("Файл application.properties не найден");
                }
                properties.load(input);
            }

            String botToken = properties.getProperty("bot.token");
            String botUsername = properties.getProperty("bot.username");
            FormHelper formhelper = new FormHelper();
            ParcelService parcelService = new ParcelService(formhelper);
            TruckService truckService = new TruckService();
            StrategyHelper strategyHelper = new StrategyHelper(parcelService);
            FileService fileService = new FileService();
            FormHelper formHelper = new FormHelper();
            OutputHelper outputHelper = new OutputHelper();
            OutputService outputService = new OutputService(truckService, outputHelper, fileService);
            CommandParserService commandParserService = new CommandParserService();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            DeliveryBot deliveryBot = new DeliveryBot(parcelService,
                    truckService,
                    strategyHelper,
                    commandParserService,
                    fileService,
                    outputService,
                    formHelper,
                    botToken,
                    botUsername);

            botsApi.registerBot(deliveryBot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
