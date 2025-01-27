package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.model.record.command.CreateCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateCommandHandlerImpl implements CommandHandler {

    private final ParcelService parcelService;
    private final CommandParserService commandParserService;
    private final FormHelper formHelper;
    private final OutputService outputService;

    @Override
    public String handle(String text) {
        try {
            CreateCommand commandData = commandParserService.parseCreateCommand(text);
            char[][] form = formHelper.parseForm(commandData.form(), commandData.symbol());

            var parcel = parcelService.create(commandData);

            return outputService.formatCreateResponse(parcel.getName(), form);
        } catch (InvalidCommandException e) {
            log.error("Invalid command", e);
            throw e;
        }
    }
}
