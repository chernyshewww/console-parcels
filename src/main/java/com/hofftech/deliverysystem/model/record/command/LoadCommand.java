package com.hofftech.deliverysystem.model.record.command;

public record LoadCommand(String user, String parcelsText, String parcelsFileName, String trucksText, String strategyType,
                          String outputType, String outputFileName) {
}
