package com.hofftech.deliverysystem.command;

public record UnloadCommand(String inputFileName, String outputFileName, boolean withCount) {
}
