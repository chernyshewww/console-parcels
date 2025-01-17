package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.model.record.UnloadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.FileService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.service.TruckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UnloadCommandHandlerImpl implements Command {

    private final ParcelService parcelService;
    private final TruckService truckService;
    private final CommandParserService commandParserService;
    private final OutputService outputService;
    private final FileService fileService;

    @Override
    public String execute(String text) {
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
            log.error("Invalid command", e);
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error while processing /unload command", e);
            return "Произошла ошибка при выгрузке посылок: " + e.getMessage();
        }
    }
}
