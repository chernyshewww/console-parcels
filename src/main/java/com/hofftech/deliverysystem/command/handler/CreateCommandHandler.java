package com.hofftech.deliverysystem.command.handler;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.command.CreateCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCommandHandler implements Command {

    private final ParcelService parcelService;
    private final CommandParserService commandParserService;
    private final FormHelper formHelper;
    private final OutputService outputService;

    @Override
    public String execute(String text) {
        try {
            CreateCommand commandData = commandParserService.parseCreateCommand(text);
            char[][] form = formHelper.parseForm(commandData.form(), commandData.symbol());
            parcelService.createParcel(commandData.name(), commandData.symbol(), form);
            return outputService.formatCreateResponse(commandData.name(), form);
        } catch (InvalidCommandException e) {
            return e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Ошибка при создании посылки: " + e.getMessage();
        }
    }
}
