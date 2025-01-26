package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.model.record.command.FindCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.ParcelRepository;
import com.hofftech.deliverysystem.repository.ParcelRepositoryInterface;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FindCommandHandlerImpl implements CommandHandler {

    private final ParcelService parcelService;
    private final CommandParserService commandParserService;

    @Override
    public String handle(String text) {
        try {
            FindCommand commandData = commandParserService.parseFindCommand(text);
            return parcelService.findByName(commandData.parcelName()).getName();
        } catch (InvalidCommandException e) {
            log.error("Invalid command", e);
            throw e;
        } catch (Exception e) {
            log.error("Error while processing /find command", e);
            return "Ошибка при поиске посылки: " + e.getMessage();
        }
    }
}
