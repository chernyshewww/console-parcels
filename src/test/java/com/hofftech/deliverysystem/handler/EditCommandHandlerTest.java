package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.EditCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.ParcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class EditCommandHandlerTest {

    @Mock
    private ParcelService parcelService;

    @Mock
    private CommandParserService commandParserService;

    private EditCommandHandlerImpl editCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        editCommandHandler = new EditCommandHandlerImpl(parcelService, commandParserService);
    }

    @Test
    void execute_ShouldReturnSuccess_WhenCommandIsValid() {
        String inputText = "/edit 1 NewParcel NewSymbol";
        EditCommand commandData = new EditCommand("OldParcel", "NewParcel", "xx\nxx", '2');

        when(commandParserService.parseEditCommand(inputText)).thenReturn(commandData);
        when(parcelService.editParcelInFile(commandData.id(), commandData.newName(), commandData.newForm(), commandData.newSymbol()))
                .thenReturn("Посылка успешно отредактирована");

        String result = editCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка успешно отредактирована");
    }

    @Test
    void execute_ShouldReturnErrorMessage_WhenCommandIsInvalid() {
        String inputText = "/edit";
        when(commandParserService.parseEditCommand(inputText)).thenThrow(new InvalidCommandException("Неверный формат команды"));

        String result = editCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Неверный формат команды");
    }

    @Test
    void execute_ShouldReturnErrorMessage_WhenParcelEditFails() {
        String inputText = "/edit 1 NewParcel NewSymbol";
        EditCommand commandData = new EditCommand("OldParcel", "NewParcel", "xx\nxx", '2');

        when(commandParserService.parseEditCommand(inputText)).thenReturn(commandData);
        when(parcelService.editParcelInFile(commandData.id(), commandData.newName(), commandData.newForm(), commandData.newSymbol()))
                .thenThrow(new IllegalArgumentException("Ошибка при редактировании посылки"));

        String result = editCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Ошибка при редактировании посылки: Ошибка при редактировании посылки");
    }
}
