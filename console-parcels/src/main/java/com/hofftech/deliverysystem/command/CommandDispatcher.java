package com.hofftech.deliverysystem.command;

import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommandDispatcher {

    private final CommandFactory commandFactory;

    public String dispatchCommand(String text) {
        String commandText = text.split(" ")[0];
        CommandHandler command;

        try {
            command = commandFactory.getCommand(commandText);
        } catch (InvalidCommandException e) {
            return e.getMessage();
        }

        return command.handle(text);
    }
}
