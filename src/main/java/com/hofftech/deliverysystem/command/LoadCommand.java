package com.hofftech.deliverysystem.command;

public record LoadCommand(String parcelsText, String parcelsFileName, String trucksText, String strategyType,
                          String outputType, String outputFileName) {
}
