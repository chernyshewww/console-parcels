package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.config.TelegramConfig;
import com.hofftech.deliverysystem.exception.BotProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeliveryBotControllerTest {

    @Mock
    private TelegramConfig telegramConfig;

    @Mock
    private CommandHandler commandHandler;

    private DeliveryBotController deliveryBotController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(telegramConfig.getToken()).thenReturn("dummy-token");
        when(telegramConfig.getUsername()).thenReturn("dummy-username");
        deliveryBotController = new DeliveryBotController(telegramConfig, commandHandler);
    }

    @Test
    void onUpdateReceived_ShouldHandleEmptyMessageText() {
        long chatId = 12345L;
        Update update = createUpdateWithMessage(chatId, null);

        assertThrows(BotProcessingException.class, () -> deliveryBotController.onUpdateReceived(update));
        verifyNoInteractions(commandHandler);
    }

    @Test
    void onUpdateReceived_ShouldHandleNullChatId() {
        Update update = createUpdateWithMessage(null, "/help");

        assertThrows(BotProcessingException.class, () -> deliveryBotController.onUpdateReceived(update));
        verifyNoInteractions(commandHandler);
    }

    @Test
    void getBotUsername_ShouldReturnConfiguredUsername() {
        String botUsername = deliveryBotController.getBotUsername();

        assertThat(botUsername).isEqualTo("dummy-username");
    }

    @Test
    void getBotToken_ShouldReturnConfiguredToken() {
        String botToken = deliveryBotController.getBotToken();

        assertThat(botToken).isEqualTo("dummy-token");
    }

    private Update createUpdateWithMessage(Long chatId, String text) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(text != null);
        when(message.getText()).thenReturn(text);
        when(message.getChatId()).thenReturn(chatId);

        return update;
    }
}
