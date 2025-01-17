package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.handler.CreateCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.DeleteCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.EditCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.FindCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.HelpCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.LoadCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.UnloadCommandHandlerImpl;
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
        try {
            switch (command) {
                case "/create":
                    return new CreateCommandHandlerImpl(parcelService, commandParserService, formHelper, outputService);
                case "/find":
                    return new FindCommandHandlerImpl(parcelService, commandParserService);
                case "/edit":
                    return new EditCommandHandlerImpl(parcelService, commandParserService);
                case "/delete":
                    return new DeleteCommandHandlerImpl(parcelService, commandParserService);
                case "/load":
                    return new LoadCommandHandlerImpl(parcelService, truckService, strategyHelper, commandParserService, outputService);
                case "/unload":
                    return new UnloadCommandHandlerImpl(parcelService, truckService, commandParserService, outputService, fileService);
                case "/help":
                    return new HelpCommandHandlerImpl();
                default:
                    throw new InvalidCommandException("Unknown command: " + command);
            }
        } catch(InvalidCommandException e){
            throw e;
        }
    }
}
