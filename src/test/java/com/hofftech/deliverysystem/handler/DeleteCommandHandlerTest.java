package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.DeleteCommand;
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

class DeleteCommandHandlerTest {

    @Mock
    private ParcelRepository parcelRepository;
    @Mock
    private CommandParserService commandParserService;

    private DeleteCommandHandlerImpl deleteCommandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteCommandHandler = new DeleteCommandHandlerImpl(parcelRepository, commandParserService);
    }

    @Test
    void execute_ShouldReturnSuccess_WhenParcelIsDeleted(){
        String inputText = "/delete Parcel123";
        DeleteCommand commandData = new DeleteCommand("Parcel123");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelRepository.deleteParcelInFile(commandData.parcelName())).thenReturn("Посылка Parcel123 удалена успешно");

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel123 удалена успешно");
    }

    @Test
    void execute_ShouldReturnError_WhenParcelDoesNotExist() {
        String inputText = "/delete ParcelXYZ";
        DeleteCommand commandData = new DeleteCommand("ParcelXYZ");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelRepository.deleteParcelInFile(commandData.parcelName())).thenReturn("Посылка ParcelXYZ не найдена");

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка ParcelXYZ не найдена");
    }

    @Test
    void execute_ShouldReturnError_WhenInvalidCommandFormat() {
        String inputText = "/delete";

        when(commandParserService.parseDeleteCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> deleteCommandHandler.execute(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }

    @Test
    void execute_ShouldReturnError_WhenDeleteCommandThrowsException() {
        String inputText = "/delete Parcel123";
        DeleteCommand commandData = new DeleteCommand("Parcel123");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelRepository.deleteParcelInFile(commandData.parcelName())).thenThrow(new IllegalArgumentException("Ошибка при удалении посылки"));

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Ошибка при удалении посылки: Ошибка при удалении посылки");
    }
}


