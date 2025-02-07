package com.hofftech.deliverysystem.billing.model.enums;

public enum Period {
    NONE,
    LAST_MONTH;

    public static Period fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
