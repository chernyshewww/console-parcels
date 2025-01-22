package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.FindCommand;
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
class FindCommandHandlerTest {

    @Mock
    private ParcelRepositoryImpl parcelRepository;

    @Mock
    private CommandParserService commandParserService;

    @InjectMocks
    private FindCommandHandlerImpl findCommandHandler;

    @Test
    @DisplayName("Должен вернуть посылку при правильном запросе")
    void execute_ShouldReturnParcel_WhenCommandIsValid() {
        String inputText = "/find Parcel1";
        FindCommand commandData = new FindCommand("Parcel1");

        when(commandParserService.parseFindCommand(inputText)).thenReturn(commandData);
        when(parcelRepository.findByName(commandData.parcelName())).thenReturn("Посылка Parcel1 найдена");

        String result = findCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel1 найдена");
    }

    @Test
    @DisplayName("Должен выбросить исключение при некорректной команде")
    void execute_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/find";

        when(commandParserService.parseFindCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> findCommandHandler.execute(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }

    @Test
    @DisplayName("Должен вернуть сообщение об ошибке, если посылка не найдена")
    void execute_ShouldReturnErrorMessage_WhenParcelNotFound() {
        String inputText = "/find Parcel1";
        FindCommand commandData = new FindCommand("Parcel1");

        when(commandParserService.parseFindCommand(inputText)).thenReturn(commandData);
        when(parcelRepository.findByName(commandData.parcelName())).thenReturn("Посылка Parcel1 не найдена");

        String result = findCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel1 не найдена");
    }
}
