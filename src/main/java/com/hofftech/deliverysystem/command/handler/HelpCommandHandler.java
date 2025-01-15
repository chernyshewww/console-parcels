package com.hofftech.deliverysystem.command.handler;

import com.hofftech.deliverysystem.command.Command;

import static com.hofftech.deliverysystem.constants.Constant.HELP_TEXT;

public class HelpCommandHandler implements Command {

    @Override
    public String execute(String text) {
        return HELP_TEXT;
    }
}
