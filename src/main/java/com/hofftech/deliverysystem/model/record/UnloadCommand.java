package com.hofftech.deliverysystem.model.record;

public record UnloadCommand(String inputFileName, String outputFileName, boolean withCount) {
}
