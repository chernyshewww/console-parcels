package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.model.record.command.EditCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class EditCommandHandlerImpl implements CommandHandler {

    private final ParcelService parcelService;
    private final CommandParserService commandParserService;
    private final FormHelper formHelper;
    private final OutputService outputService;

    @Override
    public String handle(String text) {
        try {
            EditCommand commandData = commandParserService.parseEditCommand(text);
            char[][] form = formHelper.parseForm(commandData.newForm(), commandData.newSymbol());

            var parcel = parcelService.edit(commandData);

            if (parcel != null){
                return outputService.formatCreateResponse(parcel.getName(), form);
            }
            else{
                return "Такой посылки не существует";
            }
        } catch (InvalidCommandException e) {
            log.error("Invalid command", e);
            throw e;
        } catch (Exception e) {
            log.error("Error handling /edit command", e);
            return "Ошибка при редактировании посылки: " + e.getMessage();
        }
    }
}
