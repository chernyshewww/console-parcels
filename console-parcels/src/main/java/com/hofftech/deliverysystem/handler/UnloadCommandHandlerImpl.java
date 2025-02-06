package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.model.record.command.UnloadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.service.BillingService;
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
public class UnloadCommandHandlerImpl implements CommandHandler {

    private final CommandParserService commandParserService;
    private final OutputService outputService;
    private final FileService fileService;
    private final BillingService billingService;
    private final TruckService truckService;
    private final ParcelService parcelService;

    @Override
    public String handle(String text) {
        try {
            UnloadCommand commandData = commandParserService.parseUnloadCommand(text);

            List<Long> truckIds = truckService.findAllIds();

            List<Parcel> parcels = parcelService.findParcelsByTruckIds(truckIds);

            String result = commandData.withCount()
                    ? outputService.generateParcelCountOutput(parcels)
                    : outputService.generateParcelOutput(parcels);

            fileService.saveToFile(commandData.outputFileName(), result);

            billingService.addUnloadParcelsBilling(
                    commandData.user(),
                    truckIds.size(),
                    parcels.size()
            );

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
