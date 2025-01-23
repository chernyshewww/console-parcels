package com.hofftech.deliverysystem.model.record.command;

public record EditCommand(String id, String newName, String newForm, char newSymbol) {
}
