package com.hofftech.deliverysystem.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE = "application.properties";

    public Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Файл " + CONFIG_FILE + " не найден");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения конфигурации: " + e.getMessage(), e);
        }
        return properties;
    }
}
