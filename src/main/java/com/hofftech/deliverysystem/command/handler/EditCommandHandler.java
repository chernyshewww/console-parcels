package com.hofftech.deliverysystem.command.handler;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.command.EditCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class EditCommandHandler implements Command {

    private final ParcelService parcelService;
    private final CommandParserService commandParserService;

    @Override
    public String execute(String text) {
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
}
