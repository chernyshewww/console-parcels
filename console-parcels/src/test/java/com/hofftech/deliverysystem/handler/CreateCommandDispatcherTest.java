package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.entity.ParcelEntity;
import com.hofftech.deliverysystem.model.record.command.CreateCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.ParcelRepository;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
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
    @Mock
    private ParcelService parcelService;

    @InjectMocks
    private CreateCommandHandlerImpl createCommandHandler;

    @Test
    @DisplayName("Должен вернуть успешный ответ при корректной команде")
    void handle_ShouldReturnSuccessResponse_WhenCommandIsValid() {
        // Arrange
        String inputText = "/create -name \"Посылка Тип 0\" -form \"xxx\\nxxx\\nxxx\" -symbol \"0\"";
        CreateCommand commandData = new CreateCommand("Посылка Тип 0", "xxx\\nxxx\\nxxx", '0');
        char[][] form = {{'*'}, {'*'}};
        ParcelEntity parcelEntity = new ParcelEntity();
        parcelEntity.setName(commandData.name());
        parcelEntity.setForm(commandData.form());
        parcelEntity.setSymbol(commandData.symbol());
        Parcel parcel = new Parcel(commandData.name(), commandData.symbol(), form);

        when(commandParserService.parseCreateCommand(inputText)).thenReturn(commandData);
        when(formHelper.parseForm(commandData.form(), commandData.symbol())).thenReturn(form);
        when(parcelService.create(commandData)).thenReturn(parcel);
        when(outputService.formatCreateResponse(parcel.getName(), form)).thenReturn("Посылка Посылка Тип 0 создана");

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
