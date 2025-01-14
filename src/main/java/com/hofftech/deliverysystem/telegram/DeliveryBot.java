package com.hofftech.deliverysystem.telegram;

import com.hofftech.deliverysystem.service.CommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.hofftech.deliverysystem.constants.Constant.HELP_TEXT;

@Slf4j
@RequiredArgsConstructor
public class DeliveryBot extends TelegramLongPollingBot {

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
            long chatId = message.getChatId();

            if (text.equals("/help")) {
                sendHelpMessage(chatId);
            } else {
                handleCommand(chatId, text);
            }
        }
    }

    private void sendHelpMessage(long chatId) {
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText(HELP_TEXT); // Общий текст из CommandHandler

        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);
        }
    }

    private void handleCommand(long chatId, String text) {
        String responseText = commandHandler.handleCommand(text);
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText(responseText);

        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке команды: {}", e.getMessage(), e);
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
}
