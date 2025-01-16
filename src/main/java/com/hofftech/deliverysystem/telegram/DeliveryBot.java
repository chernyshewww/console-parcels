package com.hofftech.deliverysystem.telegram;

import com.hofftech.deliverysystem.exception.BotProcessingException;
import com.hofftech.deliverysystem.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@RequiredArgsConstructor
public class DeliveryBot extends TelegramLongPollingBot {

    public static final String HELP_SHORT_TEXT = "/help";

    private final CommandHandler commandHandler;
    private final String botToken;
    private final String botUsername;

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

            if (text == null || text.isEmpty()) {
                throw new BotProcessingException("Received null or empty text in the message.");
            }

            if (chatId == null) {
                throw new BotProcessingException("Chat ID is null in the received message.");
            }

            handleCommand(chatId, text);
        }
    }

    /**
     * Returns the username of the bot. This method is required to authenticate the bot.
     *
     * @return The bot's username.
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * Returns the token of the bot. This method is required to authenticate the bot.
     *
     * @return The bot's token.
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    private void sendMessage(long chatId, String text) {
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText(text);

        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);
        }
    }

    private void handleCommand(long chatId, String text) {
        String responseText = commandHandler.handleCommand(text);
        sendMessage(chatId, responseText);
    }
}
