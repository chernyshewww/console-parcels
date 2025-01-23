package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.controller.DeliveryBotController;
import com.hofftech.deliverysystem.exception.RegisterBotException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TelegramBotInitializeServiceTest {

    @Mock
    private DeliveryBotController bot;

    @Mock
    private ContextRefreshedEvent contextRefreshedEvent;

    @InjectMocks
    private TelegramBotInitializeService telegramBotInitializeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        telegramBotInitializeService = new TelegramBotInitializeService(bot);
    }

    @Test
    @DisplayName("Должен выбросить исключение при ошибке регистрации бота")
    void initialize_ShouldThrowRegisterBotException_OnTelegramApiException() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = mock(TelegramBotsApi.class);
        doThrow(new TelegramApiException("Test Exception")).when(telegramBotsApi).registerBot(bot);

        assertThatThrownBy(() -> telegramBotInitializeService.initialize())
                .isInstanceOf(RegisterBotException.class)
                .hasMessageContaining("Ошибка регистрации бота")
                .hasCauseInstanceOf(TelegramApiException.class);
    }
}
