package com.hofftech.deliverysystem.config;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.exception.BotInitializationException;
import com.hofftech.deliverysystem.controller.DeliveryBotController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Configuration class to set up and register the Telegram bot as a Spring bean.
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class BotConfig {

    private final CommandHandler commandHandler;
    private final TelegramConfig telegramConfig;

    /**
     * Registers and returns the DeliveryBot instance as a Spring bean.
     *
     * @return the registered DeliveryBot instance.
     */
    @Bean
    public DeliveryBotController deliveryBot() {
        String botToken = telegramConfig.getToken();
        String botUsername = telegramConfig.getUsername();

        if (StringUtils.isEmpty(botToken)) {
            throw new BotInitializationException("Bot token is missing or empty in configuration");
        }

        if (StringUtils.isEmpty(botUsername)) {
            throw new BotInitializationException("Bot username is missing or empty in configuration");
        }

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            DeliveryBotController deliveryBot = new DeliveryBotController(
                    commandHandler,
                    botToken,
                    botUsername
            );

            botsApi.registerBot(deliveryBot);
            log.info("Бот успешно зарегистрирован с именем {}", botUsername);

            return deliveryBot;
        } catch (TelegramApiException e) {
            throw new BotInitializationException("Ошибка регистрации бота: ", e);
        }
    }
}

