package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.CreateCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.ParcelRepository;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.util.FormHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCommandDispatcherTest {

    @Mock
    private ParcelRepository parcelRepository;
    @Mock
    private CommandParserService commandParserService;
    @Mock
    private FormHelper formHelper;
    @Mock
    private OutputService outputService;

    @InjectMocks
    private CreateCommandHandlerImpl createCommandHandler;

    @Test
    @DisplayName("Должен вернуть успешный ответ при корректной команде")
    void handle_ShouldReturnSuccessResponse_WhenCommandIsValid() {
        String inputText = "/create -name \"Посылка Тип 0\" -form \"xxx\\nxxx\\nxxx\" -symbol \"0\"";
        CreateCommand commandData = new CreateCommand("Посылка Тип 0", "xxx\\nxxx\\nxxx", '0');
        char[][] form = {{'*'}, {'*'}};

        when(commandParserService.parseCreateCommand(inputText)).thenReturn(commandData);
        when(formHelper.parseForm(commandData.form(), commandData.symbol())).thenReturn(form);
        when(outputService.formatCreateResponse(commandData.name(), form)).thenReturn("Посылка Посылка Тип 0 создана");

        String result = createCommandHandler.handle(inputText);

        assertThat(result).isEqualTo("Посылка Посылка Тип 0 создана");
    }

    @Test
    @DisplayName("Должен вернуть ошибку при некорректном формате команды")
    void handle_ShouldReturnError_WhenInvalidCommandFormat() {
        String inputText = "/create";

        when(commandParserService.parseCreateCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> createCommandHandler.handle(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }
}
