package com.hofftech.deliverysystem.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnloadCommand {

    private final String inputFileName;
    private final String outputFileName;
    private final boolean withCount;
}
