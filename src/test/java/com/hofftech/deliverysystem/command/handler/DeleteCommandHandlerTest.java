package com.hofftech.deliverysystem.command.handler;

import com.hofftech.deliverysystem.command.DeleteCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.ParcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DeleteCommandHandlerTest {

    @Mock
    private ParcelService parcelService;
    @Mock
    private CommandParserService commandParserService;

    private DeleteCommandHandler deleteCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteCommandHandler = new DeleteCommandHandler(parcelService, commandParserService);
    }

    @Test
    void execute_ShouldReturnSuccess_WhenParcelIsDeleted(){
        String inputText = "/delete Parcel123";
        DeleteCommand commandData = new DeleteCommand("Parcel123");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelService.deleteParcelInFile(commandData.parcelName())).thenReturn("Посылка Parcel123 удалена успешно");

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel123 удалена успешно");
    }

    @Test
    void execute_ShouldReturnError_WhenParcelDoesNotExist() {
        String inputText = "/delete ParcelXYZ";
        DeleteCommand commandData = new DeleteCommand("ParcelXYZ");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelService.deleteParcelInFile(commandData.parcelName())).thenReturn("Посылка ParcelXYZ не найдена");

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка ParcelXYZ не найдена");
    }

    @Test
    void execute_ShouldReturnError_WhenInvalidCommandFormat() {
        String inputText = "/delete";

        when(commandParserService.parseDeleteCommand(inputText)).thenThrow(new InvalidCommandException("Неверный формат команды"));

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Неверный формат команды");
    }

    @Test
    void execute_ShouldReturnError_WhenDeleteCommandThrowsException() {
        String inputText = "/delete Parcel123";
        DeleteCommand commandData = new DeleteCommand("Parcel123");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelService.deleteParcelInFile(commandData.parcelName())).thenThrow(new IllegalArgumentException("Ошибка при удалении посылки"));

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Ошибка при удалении посылки: Ошибка при удалении посылки");
    }
}


