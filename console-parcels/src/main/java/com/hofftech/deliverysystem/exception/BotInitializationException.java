package com.hofftech.deliverysystem.exception;

public class BotInitializationException extends RuntimeException {

    public BotInitializationException(String message) {
        super(message);
    }

    public BotInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}