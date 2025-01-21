package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.config.TelegramConfig;
import com.hofftech.deliverysystem.exception.BotProcessingException;
import com.hofftech.deliverysystem.command.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Controller
public class DeliveryBotController extends TelegramLongPollingBot {

    private final CommandHandler commandHandler;
    private final TelegramConfig telegramConfig;

    public DeliveryBotController(TelegramConfig telegramConfig,
                                 CommandHandler commandHandler) {
        super(telegramConfig.getToken());
        this.telegramConfig = telegramConfig;
        this.commandHandler = commandHandler;
    }

    /**
     * Processes incoming updates (messages) from users. Based on the message text, the bot determines
     * which command is being issued and responds accordingly.
     *
     * @param update The incoming update containing the message sent by the user.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            Long chatId = message.getChatId();
            log.info("Received message - Chat ID: {}, Text: {}", chatId, text);

            if (StringUtils.isEmpty(text)) {
                log.error("Received null or empty text in the message. Chat ID: {}", chatId);
                throw new BotProcessingException("Received null or empty text in the message.");
            }

            if (ObjectUtils.isEmpty(chatId)) {
                log.error("Chat ID is null in the received message.");
                throw new BotProcessingException("Chat ID is null in the received message.");
            }

            try {

                var responseText = commandHandler.handleCommand(text);

                sendMessage(chatId, responseText);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sendMessage(chatId, String.format("Error while delivering: %s", e.getMessage()));
            }
        }
        else {
            throw new BotProcessingException("Received null or empty text in the message.");
        }
    }

    /**
     * Returns the username of the bot. This method is required to authenticate the bot.
     *
     * @return The bot's username.
     */
    @Override
    public String getBotUsername() {
        return telegramConfig.getUsername();
    }

    /**
     * Returns the token of the bot. This method is required to authenticate the bot.
     *
     * @return The bot's token.
     */
    @Override
    public String getBotToken() {
        return telegramConfig.getToken();
    }

     void sendMessage(long chatId, String text) {
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText(text);

        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);
        }
    }
}
