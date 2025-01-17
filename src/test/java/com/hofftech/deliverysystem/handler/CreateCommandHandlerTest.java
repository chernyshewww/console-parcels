package com.hofftech.deliverysystem.handler;
import com.hofftech.deliverysystem.model.record.CreateCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.util.FormHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CreateCommandHandlerTest {

    @Mock
    private ParcelService parcelService;
    @Mock
    private CommandParserService commandParserService;
    @Mock
    private FormHelper formHelper;
    @Mock
    private OutputService outputService;

    private CreateCommandHandlerImpl createCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createCommandHandler = new CreateCommandHandlerImpl(parcelService, commandParserService, formHelper, outputService);
    }

    @Test
    void execute_ShouldReturnSuccessResponse_WhenCommandIsValid() {
        String inputText = "/create -name \"Посылка Тип 0\" -form \"xxx\\nxxx\\nxxx\" -symbol \"0\"";
        CreateCommand commandData = new CreateCommand("Посылка Тип 0", "xxx\\nxxx\\nxxx", '0');
        char[][] form = {{'*'}, {'*'}};

        when(commandParserService.parseCreateCommand(inputText)).thenReturn(commandData);
        when(formHelper.parseForm(commandData.form(), commandData.symbol())).thenReturn(form);
        when(outputService.formatCreateResponse(commandData.name(), form)).thenReturn("Посылка Посылка Тип 0 создана");

        String result = createCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Посылка Тип 0 создана");
    }

    @Test
    void execute_ShouldReturnErrorMessage_WhenCommandIsInvalid() {
        String inputText = "/create";
        when(commandParserService.parseCreateCommand(inputText)).thenThrow(new InvalidCommandException("Неверный формат команды"));

        String result = createCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Неверный формат команды");
    }
}
