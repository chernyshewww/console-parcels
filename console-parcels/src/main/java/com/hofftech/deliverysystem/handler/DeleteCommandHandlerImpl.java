package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.model.record.command.DeleteCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.impl.ParcelRepositoryImpl;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class DeleteCommandHandlerImpl implements CommandHandler {

    private final ParcelService parcelService;

    private final CommandParserService commandParserService;

    @Override
    public String handle(String text) {
        try {
            DeleteCommand commandData = commandParserService.parseDeleteCommand(text);

            parcelService.delete(commandData.parcelName());
            return "Посылка '" + commandData.parcelName() + "' была успешно удалена";
        } catch (InvalidCommandException e) {
            log.error("Invalid command", e);
            throw e;
        } catch (Exception e) {
            log.error("Error handling /delete command", e);
            return "Ошибка при удалении посылки: " + e.getMessage();
        }
    }
}
