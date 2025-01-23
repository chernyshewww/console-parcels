package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.handler.BillingCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.CreateCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.DeleteCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.EditCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.FindCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.HelpCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.LoadCommandHandlerImpl;
import com.hofftech.deliverysystem.handler.UnloadCommandHandlerImpl;
import com.hofftech.deliverysystem.repository.impl.ParcelRepositoryImpl;
import com.hofftech.deliverysystem.repository.impl.TruckRepositoryImpl;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.util.FormHelper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
@Slf4j
public class CommandFactory {

    private final ParcelService parcelService;
    private final ParcelRepositoryImpl parcelRepository;
    private final TruckService truckService;
    private final TruckRepositoryImpl truckRepository;
    private final StrategyHelper strategyHelper;
    private final CommandParserService commandParserService;
    private final FileService fileService;
    private final OutputService outputService;
    private final FormHelper formHelper;
    private final BillingService billingService;

    private final Map<String, Supplier<CommandHandler>> commandRegistry = new HashMap<>();

    @PostConstruct
    private void init() {
        commandRegistry.put("/create", () ->
                new CreateCommandHandlerImpl(parcelRepository, commandParserService, formHelper, outputService));
        commandRegistry.put("/find", () ->
                new FindCommandHandlerImpl(parcelRepository, commandParserService));
        commandRegistry.put("/edit", () ->
                new EditCommandHandlerImpl(parcelRepository, commandParserService));
        commandRegistry.put("/delete", () ->
                new DeleteCommandHandlerImpl(parcelRepository, commandParserService));
        commandRegistry.put("/load", () ->
                new LoadCommandHandlerImpl(parcelService, truckService, strategyHelper, commandParserService, outputService, billingService));
        commandRegistry.put("/unload", () ->
                new UnloadCommandHandlerImpl(parcelService, truckRepository, commandParserService, outputService, fileService, billingService));
        commandRegistry.put("/billing", () ->
                new BillingCommandHandlerImpl(billingService, commandParserService, outputService));
        commandRegistry.put("/help", HelpCommandHandlerImpl::new);
    }

    public CommandHandler getCommand(String command) {
        Supplier<CommandHandler> commandSupplier = commandRegistry.get(command);
        if (commandSupplier == null) {
            log.error("Unknown command: {}", command);
            throw new InvalidCommandException("Unknown command: " + command);
        }
        return commandSupplier.get();
    }
}
