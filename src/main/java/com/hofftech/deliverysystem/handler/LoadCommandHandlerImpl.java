package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.model.record.command.LoadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.BillingService;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.strategy.LoadingStrategy;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class LoadCommandHandlerImpl implements CommandHandler {

    private final ParcelService parcelService;
    private final TruckService truckService;
    private final StrategyHelper strategyHelper;
    private final CommandParserService commandParserService;
    private final OutputService outputService;
    private final BillingService billingService;

    @Override
    public String handle(String text) {
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

            billingService.recordLoadOperation(
                    commandData.user(),
                    loadedTrucks.size(),
                    parcels.size()
            );

            return switch (commandData.outputType()) {
                case "text" -> outputService.generateLoadOutput(loadedTrucks);
                case "json-file" -> outputService.saveJsonOutput(commandData.outputFileName(), loadedTrucks);
                default -> "Ошибка: Неподдерживаемый тип вывода.";
            };

        } catch (InvalidCommandException e) {
            log.error("Invalid command", e);
            throw e;
        } catch (IllegalStateException e) {
            log.error("Failed to load parcels: {}", e.getMessage());
            return "Ошибка! Не удалось разместить все посылки в грузовиках. Проверьте размеры и количество.";
        } catch (Exception e) {
            log.error("Error while processing /load command", e);
            return "Произошла ошибка: " + e.getMessage();
        }
    }
}
