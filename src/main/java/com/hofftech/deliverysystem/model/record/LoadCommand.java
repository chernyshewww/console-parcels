package com.hofftech.deliverysystem.model.record;

public record LoadCommand(String parcelsText, String parcelsFileName, String trucksText, String strategyType,
                          String outputType, String outputFileName) {
}
