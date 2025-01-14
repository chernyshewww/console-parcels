package com.hofftech.deliverysystem.command;

public record EditCommand(String id, String newName, String newForm, char newSymbol) {
}
