package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.EditCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.impl.ParcelRepositoryImpl;
import com.hofftech.deliverysystem.service.CommandParserService;
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
class EditCommandHandlerTest {

    @Mock
    private ParcelRepositoryImpl parcelRepository;

    @Mock
    private CommandParserService commandParserService;

    @InjectMocks
    private EditCommandHandlerImpl editCommandHandler;

    @Test
    @DisplayName("Должен вернуть успешный ответ при успешном редактировании посылки")
    void execute_ShouldReturnSuccess_WhenCommandIsValid() {
        String inputText = "/edit 1 NewParcel NewSymbol";
        EditCommand commandData = new EditCommand("OldParcel", "NewParcel", "xx\nxx", '2');

        when(commandParserService.parseEditCommand(inputText)).thenReturn(commandData);
        when(parcelRepository.update(commandData.id(), commandData.newName(), commandData.newForm(), commandData.newSymbol()))
                .thenReturn("Посылка успешно отредактирована");

        String result = editCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка успешно отредактирована");
    }

    @Test
    @DisplayName("Должен вернуть ошибку при некорректном формате команды")
    void execute_ShouldReturnErrorMessage_WhenCommandIsInvalid() {
        String inputText = "/edit";

        when(commandParserService.parseEditCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> editCommandHandler.execute(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если редактирование посылки не удалось")
    void execute_ShouldReturnErrorMessage_WhenParcelEditFails() {
        String inputText = "/edit 1 NewParcel NewSymbol";
        EditCommand commandData = new EditCommand("OldParcel", "NewParcel", "xx\nxx", '2');

        when(commandParserService.parseEditCommand(inputText)).thenReturn(commandData);
        when(parcelRepository.update(commandData.id(), commandData.newName(), commandData.newForm(), commandData.newSymbol()))
                .thenThrow(new IllegalArgumentException("Ошибка при редактировании посылки"));

        String result = editCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Ошибка при редактировании посылки: Ошибка при редактировании посылки");
    }
}
