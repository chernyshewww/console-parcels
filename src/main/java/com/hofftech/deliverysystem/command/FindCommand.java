package com.hofftech.deliverysystem.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindCommand {

    private final String parcelName;
}
