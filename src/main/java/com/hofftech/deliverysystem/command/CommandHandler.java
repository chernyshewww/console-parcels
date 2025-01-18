package com.hofftech.deliverysystem.command;

import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
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
