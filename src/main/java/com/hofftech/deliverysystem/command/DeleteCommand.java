package com.hofftech.deliverysystem.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteCommand {

    private final String parcelName;
}
