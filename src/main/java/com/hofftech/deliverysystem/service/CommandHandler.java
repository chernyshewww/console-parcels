package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.command.CreateCommand;
import com.hofftech.deliverysystem.command.DeleteCommand;
import com.hofftech.deliverysystem.command.EditCommand;
import com.hofftech.deliverysystem.command.FindCommand;
import com.hofftech.deliverysystem.command.LoadCommand;
import com.hofftech.deliverysystem.command.UnloadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.strategy.LoadingStrategy;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.hofftech.deliverysystem.constants.Constant.HELP_TEXT;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final ParcelService parcelService;
    private final TruckService truckService;
    private final StrategyHelper strategyHelper;
    private final CommandParserService commandParserService;
    private final FileService fileService;
    private final OutputService outputService;
    private final FormHelper formHelper;

    private final Map<String, Function<String, String>> commandMap;

    public CommandHandler(ParcelService parcelService, TruckService truckService, StrategyHelper strategyHelper,
                          CommandParserService commandParserService, FileService fileService, OutputService outputService,
                          FormHelper formHelper) {
        this.parcelService = parcelService;
        this.truckService = truckService;
        this.strategyHelper = strategyHelper;
        this.commandParserService = commandParserService;
        this.fileService = fileService;
        this.outputService = outputService;
        this.formHelper = formHelper;

        this.commandMap = new HashMap<>();
        this.commandMap.put("/help", text -> HELP_TEXT);
        this.commandMap.put("/create", this::handleCreateCommand);
        this.commandMap.put("/find", this::handleFindCommand);
        this.commandMap.put("/edit", this::handleEditCommand);
        this.commandMap.put("/load", this::handleLoadCommand);
        this.commandMap.put("/unload", this::handleUnloadCommand);
        this.commandMap.put("/delete", this::handleDeleteCommand);
    }

    public String handleCommand(String text) {
        String command = text.split(" ")[0];
        Function<String, String> handler = commandMap.get(command);

        if (handler == null) {
            return "Команда не распознана. Пожалуйста, используйте правильный формат.";
        }
        return handler.apply(text);
    }


    private String handleCreateCommand(String text) {
        try {
            CreateCommand commandData = commandParserService.parseCreateCommand(text);
            char[][] form = formHelper.parseForm(commandData.form(), commandData.symbol());

            parcelService.createParcel(commandData.name(), commandData.symbol(), form);

            return outputService.formatCreateResponse(commandData.name(), form);
        } catch (InvalidCommandException e) {
            return e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Ошибка при создании посылки: " + e.getMessage();
        }
    }

    private String handleFindCommand(String text) {
        try {
            FindCommand commandData = commandParserService.parseFindCommand(text);
            return parcelService.findParcelInFile(commandData.parcelName());
        } catch (InvalidCommandException e) {
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error while processing /find command", e);
            return "Ошибка при поиске посылки: " + e.getMessage();
        }
    }

    private String handleEditCommand(String text) {
        try {
            EditCommand commandData = commandParserService.parseEditCommand(text);

            return parcelService.editParcelInFile(
                    commandData.id(),
                    commandData.newName(),
                    commandData.newForm(),
                    commandData.newSymbol()
            );
        } catch (InvalidCommandException e) {
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error handling /edit command", e);
            return "Ошибка при редактировании посылки: " + e.getMessage();
        }
    }

    private String handleDeleteCommand(String text) {
        try {
            DeleteCommand commandData = commandParserService.parseDeleteCommand(text);

            return parcelService.deleteParcelInFile(commandData.parcelName());
        } catch (InvalidCommandException e) {
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error handling /delete command", e);
            return "Ошибка при удалении посылки: " + e.getMessage();
        }
    }

    private String handleLoadCommand(String text) {
        try {
            LoadCommand commandData = commandParserService.parseLoadCommand(text);
            List<Parcel> parcels = parcelService.loadParcels(commandData);
            List<Truck> trucks = truckService.parseTruckSizes(commandData.trucksText());
            LoadingStrategy strategy = strategyHelper.determineStrategy(commandData.strategyType());

            if (strategy == null) {
                return "Ошибка! Указан неизвестный тип стратегии.";
            }

            List<Truck> loadedTrucks = strategy.loadParcels(parcels, trucks);
            log.info("Successfully loaded parcels.");

            switch (commandData.outputType()) {
                case "text":
                    return outputService.generateLoadOutput(loadedTrucks);
                case "json-file":
                    outputService.saveJsonOutput(commandData.outputFileName(), loadedTrucks, null);
                    return "Результат сохранён в файл: " + commandData.outputFileName();
                default:
                    return "Ошибка: Неподдерживаемый тип вывода.";
            }
        } catch (InvalidCommandException e) {
            return e.getMessage();
        } catch (IllegalStateException e) {
            log.error("Failed to load parcels: {}", e.getMessage());
            return "Ошибка! Не удалось разместить все посылки в грузовиках. Проверьте размеры и количество.";
        } catch (Exception e) {
            log.error("Error while processing /load command", e);
            return "Произошла ошибка: " + e.getMessage();
        }
    }

    private String handleUnloadCommand(String text) {
        try {
            UnloadCommand commandData = commandParserService.parseUnloadCommand(text);

            List<Truck> trucks = truckService.loadTrucksFromFile(commandData.inputFileName());

            List<Parcel> parcels = parcelService.unloadParcelsFromTrucks(trucks);

            String result = commandData.withCount()
                    ? outputService.generateParcelCountOutput(parcels)
                    : outputService.generateParcelOutput(parcels);

            fileService.saveToFile(commandData.outputFileName(), result);

            return "Выгрузка завершена. Результат сохранён в файл: " + commandData.outputFileName();
        } catch (InvalidCommandException e) {
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error while processing /unload command", e);
            return "Произошла ошибка при выгрузке посылок: " + e.getMessage();
        }
    }
}
