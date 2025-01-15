package com.hofftech.deliverysystem.exception;

public class BotProcessingException extends RuntimeException {
    public BotProcessingException(String message) {
        super(message);
    }

    public BotProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}