package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.command.CommandFactory;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CommandHandler {

    private final CommandFactory commandFactory;

    public String handleCommand(String text) {
        String command = text.split(" ")[0];
        Command commandHandler;

        try {
            commandHandler = commandFactory.getCommandHandler(command);
        } catch (InvalidCommandException e) {
            return e.getMessage();
        }

        return commandHandler.execute(text);
    }
}
