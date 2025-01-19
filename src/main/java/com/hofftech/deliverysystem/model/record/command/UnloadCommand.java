package com.hofftech.deliverysystem.model.record.command;

public record UnloadCommand(String user, String inputFileName, String outputFileName, boolean withCount) {
}
