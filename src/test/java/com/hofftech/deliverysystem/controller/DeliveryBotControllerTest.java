package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.command.CommandDispatcher;
import com.hofftech.deliverysystem.config.TelegramConfig;
import com.hofftech.deliverysystem.exception.BotProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class DeliveryBotControllerTest {

    @Mock
    private TelegramConfig telegramConfig;

    @Mock
    private CommandDispatcher commandDispatcher;

    @InjectMocks
    private DeliveryBotController deliveryBotController;

    @Test
    @DisplayName("Должен обработать сообщение с пустым текстом")
    void onUpdateReceived_ShouldHandleEmptyMessageText() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setText(null);

        assertThrows(BotProcessingException.class, () -> deliveryBotController.onUpdateReceived(update));
        verifyNoInteractions(commandDispatcher);
    }

    @Test
    @DisplayName("Должен обработать сообщение с отсутствующим chatId")
    void onUpdateReceived_ShouldHandleNullChatId() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setText("/help");
        message.setChat(null);

        assertThrows(NullPointerException.class, () -> deliveryBotController.onUpdateReceived(update));
        verifyNoInteractions(commandDispatcher);
    }
}
