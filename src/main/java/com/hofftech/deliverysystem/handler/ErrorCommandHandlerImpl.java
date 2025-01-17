package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.Command;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorCommandHandlerImpl implements Command {

    /**
     * Handles error messages and returns a formatted response.
     *
     * @param errorMessage The error message to process.
     * @return A formatted error response.
     */
    @Override
    public String execute(String errorMessage) {
        log.error("Error received: {}", errorMessage);

        return formatErrorResponse(errorMessage);
    }

    /**
     * Formats the error message into a user-friendly response.
     *
     * @param errorMessage The raw error message.
     * @return A formatted error response.
     */
    private String formatErrorResponse(String errorMessage) {
        return "❌ Error: " + errorMessage;
    }
}