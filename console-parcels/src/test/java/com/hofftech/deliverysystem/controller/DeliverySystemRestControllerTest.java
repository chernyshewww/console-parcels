package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.command.CommandDispatcher;
import com.hofftech.deliverysystem.service.LoadCommandService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class DeliverySystemRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommandDispatcher commandDispatcher;

    @MockitoBean
    private LoadCommandService loadCommandService;

    @Test
    void create_withValidRequest_shouldReturnValidResponse() throws Exception {

        String name = "Посылка Тип 0";
        String form = "xxx\nxxx\nxxx";
        char symbol = '0';
        String expectedResponse = "Посылка Посылка Тип 0 создана";

        Mockito.when(commandDispatcher.dispatchCommand(Mockito.anyString()))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/create")
                        .param("name", name)
                        .param("form", form)
                        .param("symbol", String.valueOf(symbol))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        Mockito.verify(commandDispatcher).dispatchCommand(
                String.format("/create -name \"%s\" -form \"%s\" -symbol \"%c\"", name, form, symbol)
        );
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