package com.hofftech.deliverysystem.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramConfigTest {

    @Test
    void testSetAndGetToken() {
        TelegramConfig telegramConfig = new TelegramConfig();

        telegramConfig.setToken("test-token");

        assertThat(telegramConfig.getToken()).isEqualTo("test-token");
    }

    @Test
    void testSetAndGetUsername() {
        TelegramConfig telegramConfig = new TelegramConfig();

        telegramConfig.setUsername("test-username");

        assertThat(telegramConfig.getUsername()).isEqualTo("test-username");
    }

    @Test
    void testDefaultValues() {
        TelegramConfig telegramConfig = new TelegramConfig();

        assertThat(telegramConfig.getToken()).isNull();
        assertThat(telegramConfig.getUsername()).isNull();
    }
}
