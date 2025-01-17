package com.hofftech.deliverysystem.telegram;

import com.hofftech.deliverysystem.service.CommandFactory;
import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.FileService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.util.FormHelper;
import com.hofftech.deliverysystem.util.OutputHelper;

public class BotFactory {

    public static CommandHandler createCommandHandler() {
        return new CommandHandler(
                createCommandFactory()
        );
    }

    public static CommandFactory createCommandFactory() {
        return new CommandFactory(createParcelService(),
                createTruckService(),
                createStrategyHelper(),
                createCommandParserService(),
                createFileService(),
                createOutputService(),
                createFormHelper());
    }

    public static ParcelService createParcelService() {
        return new ParcelService(createFormHelper());
    }

    public static TruckService createTruckService() {
        return new TruckService();
    }

    public static StrategyHelper createStrategyHelper() {
        return new StrategyHelper(createParcelService());
    }

    public static CommandParserService createCommandParserService() {
        return new CommandParserService();
    }

    public static FileService createFileService() {
        return new FileService();
    }

    public static OutputHelper createOutputHelper() {
        return new OutputHelper();
    }

    public static OutputService createOutputService() {
        return new OutputService(
                createTruckService(),
                createOutputHelper(),
                createFileService()
        );
    }

    public static FormHelper createFormHelper() {
        return new FormHelper();
    }
}