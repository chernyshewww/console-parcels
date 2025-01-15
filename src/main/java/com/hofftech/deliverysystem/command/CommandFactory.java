package com.hofftech.deliverysystem.command;

import com.hofftech.deliverysystem.command.handler.CreateCommandHandler;
import com.hofftech.deliverysystem.command.handler.DeleteCommandHandler;
import com.hofftech.deliverysystem.command.handler.EditCommandHandler;
import com.hofftech.deliverysystem.command.handler.FindCommandHandler;
import com.hofftech.deliverysystem.command.handler.HelpCommandHandler;
import com.hofftech.deliverysystem.command.handler.LoadCommandHandler;
import com.hofftech.deliverysystem.command.handler.UnloadCommandHandler;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.FileService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandFactory {

    private final ParcelService parcelService;
    private final TruckService truckService;
    private final StrategyHelper strategyHelper;
    private final CommandParserService commandParserService;
    private final FileService fileService;
    private final OutputService outputService;
    private final FormHelper formHelper;

    public Command getCommandHandler(String command) {
        switch (command) {
            case "/create":
                return new CreateCommandHandler(parcelService, commandParserService, formHelper, outputService);
            case "/find":
                return new FindCommandHandler(parcelService, commandParserService);
            case "/edit":
                return new EditCommandHandler(parcelService, commandParserService);
            case "/delete":
                return new DeleteCommandHandler(parcelService, commandParserService);
            case "/load":
                return new LoadCommandHandler(parcelService, truckService, strategyHelper, commandParserService, outputService);
            case "/unload":
                return new UnloadCommandHandler(parcelService, truckService, commandParserService, outputService, fileService);
            case "/help":
                return new HelpCommandHandler();
            default:
                throw new InvalidCommandException("Unknown command: " + command);
        }
    }
}
