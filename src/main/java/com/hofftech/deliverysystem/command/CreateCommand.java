package com.hofftech.deliverysystem.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCommand {

    private final String name;
    private final String form;
    private final char symbol;
}
