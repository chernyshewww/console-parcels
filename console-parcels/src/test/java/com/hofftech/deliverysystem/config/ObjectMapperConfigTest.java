package com.hofftech.deliverysystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ObjectMapperConfigTest {

    @Test
    @DisplayName("ObjectMapper должен быть настроен с включенной функцией INDENT_OUTPUT")
    void testObjectMapperConfig() {
        ObjectMapperConfig objectMapperConfig = new ObjectMapperConfig();

        ObjectMapper objectMapper = objectMapperConfig.objectMapper();

        assertNotNull(objectMapper, "ObjectMapper не должен быть равен null.");
        assertTrue(objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT),
                "Функция INDENT_OUTPUT должна быть включена.");
    }
}
