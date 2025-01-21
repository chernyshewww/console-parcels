package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.FindCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.ParcelRepository;
import com.hofftech.deliverysystem.service.CommandParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

class FindCommandHandlerTest {

    @Mock
    private ParcelRepository parcelRepository;

    @Mock
    private CommandParserService commandParserService;

    private FindCommandHandlerImpl findCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        findCommandHandler = new FindCommandHandlerImpl(parcelRepository, commandParserService);
    }

    @Test
    void execute_ShouldReturnParcel_WhenCommandIsValid() {
        String inputText = "/find Parcel1";
        FindCommand commandData = new FindCommand("Parcel1");

        when(commandParserService.parseFindCommand(inputText)).thenReturn(commandData);
        when(parcelRepository.findParcelInFile(commandData.parcelName())).thenReturn("Посылка Parcel1 найдена");

        String result = findCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel1 найдена");
    }

    @Test
    void execute_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/find";

        when(commandParserService.parseFindCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> findCommandHandler.execute(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }

    @Test
    void execute_ShouldReturnErrorMessage_WhenParcelNotFound() {
        String inputText = "/find Parcel1";
        FindCommand commandData = new FindCommand("Parcel1");

        when(commandParserService.parseFindCommand(inputText)).thenReturn(commandData);
        when(parcelRepository.findParcelInFile(commandData.parcelName())).thenReturn("Посылка Parcel1 не найдена");

        String result = findCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel1 не найдена");
    }
}
