package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.Command;
import com.hofftech.deliverysystem.constants.Constant;


public class HelpCommandHandlerImpl implements Command {

    @Override
    public String execute(String text) {
        return Constant.HELP_TEXT.getValue().toString();
    }
}
