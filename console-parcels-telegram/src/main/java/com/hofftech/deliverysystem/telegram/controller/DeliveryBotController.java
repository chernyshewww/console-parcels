package com.hofftech.deliverysystem.telegram.controller;

import com.hofftech.deliverysystem.telegram.exception.BotProcessingException;
import com.hofftech.deliverysystem.telegram.config.TelegramConfig;
import com.hofftech.deliverysystem.telegram.service.DeliverySystemDispatcherService;
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

    private final DeliverySystemDispatcherService deliverySystemDispatcherService;
    private final TelegramConfig telegramConfig;

    public DeliveryBotController(TelegramConfig telegramConfig,
                                 DeliverySystemDispatcherService deliverySystemDispatcherService) {
        super(telegramConfig.getToken());
        this.telegramConfig = telegramConfig;
        this.deliverySystemDispatcherService = deliverySystemDispatcherService;
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

                var responseText = deliverySystemDispatcherService.dispatchCommand(text);

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
