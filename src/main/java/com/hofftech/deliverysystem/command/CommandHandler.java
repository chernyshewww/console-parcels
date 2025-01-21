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
        String commandText = text.split(" ")[0];
        Command command;

        try {
            command = commandFactory.getCommand(commandText);
        } catch (InvalidCommandException e) {
            return e.getMessage();
        }

        return command.execute(text);
    }
}
