package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.entity.ParcelEntity;
import com.hofftech.deliverysystem.model.record.command.FindCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.ParcelRepository;
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
class FindCommandDispatcherTest {

    @Mock
    private ParcelRepository parcelRepository;

    @Mock
    private CommandParserService commandParserService;

    @InjectMocks
    private FindCommandHandlerImpl findCommandHandler;

    @Test
    @DisplayName("Должен вернуть посылку при правильном запросе")
    void handle_ShouldReturnParcel_WhenCommandIsValid() {
        String inputText = "/find Parcel1";
        FindCommand commandData = new FindCommand("Parcel1");

        when(commandParserService.parseFindCommand(inputText)).thenReturn(commandData);
        when(parcelRepository.findByName(commandData.parcelName())).thenReturn(new ParcelEntity());

        String result = findCommandHandler.handle(inputText);

        assertThat(result).isEqualTo(new ParcelEntity());
    }

    @Test
    @DisplayName("Должен выбросить исключение при некорректной команде")
    void handle_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/find";

        when(commandParserService.parseFindCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> findCommandHandler.handle(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }
}
