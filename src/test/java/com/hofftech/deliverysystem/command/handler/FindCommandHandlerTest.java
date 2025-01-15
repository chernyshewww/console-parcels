package com.hofftech.deliverysystem.command.handler;

import com.hofftech.deliverysystem.command.FindCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.ParcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FindCommandHandlerTest {

    @Mock
    private ParcelService parcelService;

    @Mock
    private CommandParserService commandParserService;

    private FindCommandHandler findCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        findCommandHandler = new FindCommandHandler(parcelService, commandParserService);
    }

    @Test
    void execute_ShouldReturnParcel_WhenCommandIsValid() {
        String inputText = "/find Parcel1";
        FindCommand commandData = new FindCommand("Parcel1");

        when(commandParserService.parseFindCommand(inputText)).thenReturn(commandData);
        when(parcelService.findParcelInFile(commandData.parcelName())).thenReturn("Посылка Parcel1 найдена");

        String result = findCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel1 найдена");
    }

    @Test
    void execute_ShouldReturnErrorMessage_WhenCommandIsInvalid() {
        String inputText = "/find";
        when(commandParserService.parseFindCommand(inputText)).thenThrow(new InvalidCommandException("Неверный формат команды"));

        String result = findCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Неверный формат команды");
    }

    @Test
    void execute_ShouldReturnErrorMessage_WhenParcelNotFound() {
        String inputText = "/find Parcel1";
        FindCommand commandData = new FindCommand("Parcel1");

        when(commandParserService.parseFindCommand(inputText)).thenReturn(commandData);
        when(parcelService.findParcelInFile(commandData.parcelName())).thenReturn("Посылка Parcel1 не найдена");

        String result = findCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel1 не найдена");
    }
}
