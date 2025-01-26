package com.hofftech.deliverysystem.telegram.exception;

public class InvalidCommandException extends RuntimeException {

    public InvalidCommandException(String message) {
        super(message);
    }
}
