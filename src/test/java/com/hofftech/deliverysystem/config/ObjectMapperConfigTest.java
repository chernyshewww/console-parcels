package com.hofftech.deliverysystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ObjectMapperConfigTest {

    @Test
    void testObjectMapperConfig() {
        ObjectMapperConfig objectMapperConfig = new ObjectMapperConfig();

        ObjectMapper objectMapper = objectMapperConfig.objectMapper();

        assertNotNull(objectMapper, "ObjectMapper should not be null.");
        assertTrue(objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT),
                "INDENT_OUTPUT feature should be enabled.");
    }
}
