package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.telegram.DeliveryBot;
import com.hofftech.deliverysystem.util.ConfigLoader;
import com.hofftech.deliverysystem.util.FormHelper;
import com.hofftech.deliverysystem.util.OutputHelper;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Properties;

/**
 * The BotInitializer class is responsible for initializing the bot, setting up its required services,
 * and registering it with the Telegram API.
 * <p>
 * Upon initialization, it loads the configuration, creates all necessary services and components required for
 * the bot to function, and registers the bot with Telegram.
 * </p>
 */
@Slf4j
public class BotInitializer {

    private final ConfigLoader configLoader;

    public BotInitializer(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    /**
     * Initializes all services and components required for the bot's functionality.
     * It loads the configuration, creates the necessary services and objects, and registers the bot with Telegram's API.
     *
     * <p>This method performs the following steps:</p>
     * <ul>
     *     <li>Loads the bot's token and username from the configuration.</li>
     *     <li>Creates services for managing parcels, trucks, and distribution strategies.</li>
     *     <li>Initializes the command handler.</li>
     *     <li>Registers the bot using the Telegram API.</li>
     * </ul>
     *
     * @throws RuntimeException If the bot registration fails.
     */
    public void initialize() {
        Properties properties = configLoader.loadConfig();

        String botToken = properties.getProperty("bot.token");
        String botUsername = properties.getProperty("bot.username");

        FormHelper formHelper = new FormHelper();
        ParcelService parcelService = new ParcelService(formHelper);
        TruckService truckService = new TruckService();
        StrategyHelper strategyHelper = new StrategyHelper(parcelService);
        FileService fileService = new FileService();
        OutputHelper outputHelper = new OutputHelper();
        OutputService outputService = new OutputService(truckService, outputHelper, fileService);
        CommandParserService commandParserService = new CommandParserService();

        CommandHandler commandHandler = new CommandHandler(
                parcelService,
                truckService,
                strategyHelper,
                commandParserService,
                fileService,
                outputService,
                formHelper);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            DeliveryBot deliveryBot = new DeliveryBot(
                    commandHandler,
                    botToken,
                    botUsername
            );

            botsApi.registerBot(deliveryBot);
            log.info("Бот успешно зарегистрирован с именем {}", botUsername);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка регистрации бота: ", e);
        }
    }
}