package com.hofftech.deliverysystem.model.record;

public record EditCommand(String id, String newName, String newForm, char newSymbol) {
}
