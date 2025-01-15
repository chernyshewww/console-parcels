package com.hofftech.deliverysystem;

import com.hofftech.deliverysystem.service.BotFactory;
import com.hofftech.deliverysystem.service.BotInitializer;
import com.hofftech.deliverysystem.util.ConfigLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try {
            BotInitializer botInitializer = new BotInitializer(new ConfigLoader(), BotFactory.createCommandHandler());
            botInitializer.registerBot();
        } catch (Exception e) {
            log.error("Ошибка инициализации бота: ", e);
        }
    }
}
