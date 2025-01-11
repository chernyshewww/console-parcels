package com.hofftech.deliverysystem.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditCommand {

    private final String id;
    private final String newName;
    private final String newForm;
    private final char newSymbol;
}
