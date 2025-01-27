package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.model.record.command.FindCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class FindCommandHandlerImpl implements CommandHandler {

    private final ParcelService parcelService;
    private final CommandParserService commandParserService;
    private final FormHelper formHelper;
    private final OutputService outputService;

    @Override
    public String handle(String text) {
        try {
            FindCommand commandData = commandParserService.parseFindCommand(text);
            var foundParcel = parcelService.findByName(commandData.parcelName());

            if (foundParcel == null) {
                return "Посылка не была найдена";
            }
            char[][] form = formHelper.parseForm(formHelper.getFormAsString(foundParcel.getForm()), foundParcel.getSymbol());

            return outputService.formatCreateResponse(foundParcel.getName(), form);
        } catch (InvalidCommandException e) {
            log.error("Invalid command", e);
            throw e;
        } catch (Exception e) {
            log.error("Error while processing /find command", e);
            return "Ошибка при поиске посылки: " + e.getMessage();
        }
    }
}
