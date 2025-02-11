package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.container.AbstractPostgresContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class DeliverySystemRestControllerIT  extends AbstractPostgresContainer {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create_withValidRequest_shouldReturnValidResponse() throws Exception {
        String name = "Посылка Тип 0";
        String form = "xxx\nxxx\nxxx";
        char symbol = '0';
        String expectedResponse = "Посылка Посылка Тип 0 создана";

        mockMvc.perform(post("/create")
                        .param("name", name)
                        .param("form", form)
                        .param("symbol", String.valueOf(symbol))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void create_withInvalidRequest_shouldReturnBadRequest() throws Exception {
        String name = "";
        String form = "xxx\nxxx\nxxx";
        char symbol = '0';

        mockMvc.perform(post("/create")
                        .param("name", name)
                        .param("form", form)
                        .param("symbol", String.valueOf(symbol))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }
}