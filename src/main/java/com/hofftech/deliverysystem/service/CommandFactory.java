package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.handler.BillingCommandHandlerImpl;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CommandFactory {

    private final ParcelService parcelService;
    private final TruckService truckService;
    private final StrategyHelper strategyHelper;
    private final CommandParserService commandParserService;
    private final FileService fileService;
    private final OutputService outputService;
    private final FormHelper formHelper;
    private final BillingService billingService;

    public Command getCommandHandler(String command) {
        try {
            return switch (command) {
                case "/create" ->
                        new CreateCommandHandlerImpl(parcelService, commandParserService, formHelper, outputService);
                case "/find" -> new FindCommandHandlerImpl(parcelService, commandParserService);
                case "/edit" -> new EditCommandHandlerImpl(parcelService, commandParserService);
                case "/delete" -> new DeleteCommandHandlerImpl(parcelService, commandParserService);
                case "/load" ->
                        new LoadCommandHandlerImpl(parcelService, truckService, strategyHelper, commandParserService, outputService, billingService);
                case "/unload" ->
                        new UnloadCommandHandlerImpl(parcelService, truckService, commandParserService, outputService, fileService, billingService);
                case "/billing" -> new BillingCommandHandlerImpl(billingService, commandParserService, outputService);
                case "/help" -> new HelpCommandHandlerImpl();
                default -> throw new InvalidCommandException("Unknown command: " + command);
            };
        } catch(InvalidCommandException e){
            log.error("Error while processing command:", e);
            throw e;
        }
    }
}
