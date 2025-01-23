package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.constants.Constant;


public class HelpCommandHandlerImpl implements CommandHandler {

    @Override
    public String handle(String text) {
        return Constant.HELP_TEXT.getValue().toString();
    }
}
