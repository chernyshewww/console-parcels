package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.model.record.command.DeleteCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.ParcelRepository;
import com.hofftech.deliverysystem.service.CommandParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class DeleteCommandHandlerImpl implements Command {

    private final ParcelRepository parcelRepository;
    private final CommandParserService commandParserService;

    @Override
    public String execute(String text) {
        try {
            DeleteCommand commandData = commandParserService.parseDeleteCommand(text);

            return parcelRepository.deleteParcelInFile(commandData.parcelName());
        } catch (InvalidCommandException e) {
            log.error("Invalid command", e);
            throw e;
        } catch (Exception e) {
            log.error("Error handling /delete command", e);
            return "Ошибка при удалении посылки: " + e.getMessage();
        }
    }
}
