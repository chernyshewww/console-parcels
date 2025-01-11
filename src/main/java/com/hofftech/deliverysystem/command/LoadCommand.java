package com.hofftech.deliverysystem.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoadCommand {

    private final String parcelsText;
    private final String parcelsFileName;
    private final String trucksText;
    private final String strategyType;
    private final String outputType;
    private final String outputFileName;
}
