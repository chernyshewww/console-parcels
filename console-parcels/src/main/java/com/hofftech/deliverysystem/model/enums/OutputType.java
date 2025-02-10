package com.hofftech.deliverysystem.model.enums;

public enum OutputType {
    TEXT, JSON_FILE;

    public static OutputType fromString(String type) {
        try {
            return OutputType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException("Ошибка: Неподдерживаемый тип вывода.");
        }
    }
}