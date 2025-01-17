package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.model.record.FindCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FindCommandHandlerImpl implements Command {

    private final ParcelService parcelService;
    private final CommandParserService commandParserService;

    @Override
    public String execute(String text) {
        try {
            FindCommand commandData = commandParserService.parseFindCommand(text);
            return parcelService.findParcelInFile(commandData.parcelName());
        } catch (
                InvalidCommandException e) {
            return e.getMessage();
        } catch (Exception e) {
            log.error("Error while processing /find command", e);
            return "Ошибка при поиске посылки: " + e.getMessage();
        }
    }
}
