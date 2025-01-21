package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.controller.DeliveryBotController;
import com.hofftech.deliverysystem.exception.RegisterBotException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class TelegramBotInitializeService {

    private final DeliveryBotController bot;

    @EventListener({ContextRefreshedEvent.class})
    public void initialize() {
        try{
            new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RegisterBotException("Ошибка регистрации бота", e);
        }
    }
}
