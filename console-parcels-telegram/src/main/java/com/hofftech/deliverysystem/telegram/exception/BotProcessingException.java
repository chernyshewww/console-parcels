package com.hofftech.deliverysystem.telegram.exception;

public class BotProcessingException extends RuntimeException {

    public BotProcessingException(String message) {
        super(message);
    }
}