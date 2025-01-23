package com.hofftech.deliverysystem.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramConfigTest {

    @Test
    @DisplayName("Метод setToken и getToken должны работать корректно")
    void testSetAndGetToken() {
        TelegramConfig telegramConfig = new TelegramConfig();

        telegramConfig.setToken("test-token");

        assertThat(telegramConfig.getToken()).isEqualTo("test-token");
    }

    @Test
    @DisplayName("Метод setUsername и getUsername должны работать корректно")
    void testSetAndGetUsername() {
        TelegramConfig telegramConfig = new TelegramConfig();

        telegramConfig.setUsername("test-username");

        assertThat(telegramConfig.getUsername()).isEqualTo("test-username");
    }

    @Test
    @DisplayName("Проверка значений по умолчанию")
    void testDefaultValues() {
        TelegramConfig telegramConfig = new TelegramConfig();

        assertThat(telegramConfig.getToken()).isNull();
        assertThat(telegramConfig.getUsername()).isNull();
    }
}
