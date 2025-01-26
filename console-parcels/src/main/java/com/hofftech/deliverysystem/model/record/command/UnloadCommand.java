package com.hofftech.deliverysystem.model.record.command;

public record UnloadCommand(String user, String outputFileName, boolean withCount) {
}
