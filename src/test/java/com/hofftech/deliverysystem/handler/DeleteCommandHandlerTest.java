package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.record.command.DeleteCommand;
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
class DeleteCommandHandlerTest {

    @Mock
    private ParcelRepositoryImpl parcelRepository;
    @Mock
    private CommandParserService commandParserService;

    @InjectMocks
    private DeleteCommandHandlerImpl deleteCommandHandler;

    @Test
    @DisplayName("Должен вернуть успешный ответ при удалении посылки")
    void execute_ShouldReturnSuccess_WhenParcelIsDeleted() {
        String inputText = "/delete Parcel123";
        DeleteCommand commandData = new DeleteCommand("Parcel123");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelRepository.deleteByName(commandData.parcelName())).thenReturn("Посылка Parcel123 удалена успешно");

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка Parcel123 удалена успешно");
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если посылка не найдена")
    void execute_ShouldReturnError_WhenParcelDoesNotExist() {
        String inputText = "/delete ParcelXYZ";
        DeleteCommand commandData = new DeleteCommand("ParcelXYZ");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelRepository.deleteByName(commandData.parcelName())).thenReturn("Посылка ParcelXYZ не найдена");

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Посылка ParcelXYZ не найдена");
    }

    @Test
    @DisplayName("Должен вернуть ошибку при некорректном формате команды")
    void execute_ShouldReturnError_WhenInvalidCommandFormat() {
        String inputText = "/delete";

        when(commandParserService.parseDeleteCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> deleteCommandHandler.execute(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }

    @Test
    @DisplayName("Должен вернуть ошибку при исключении в процессе удаления")
    void execute_ShouldReturnError_WhenDeleteCommandThrowsException() {
        String inputText = "/delete Parcel123";
        DeleteCommand commandData = new DeleteCommand("Parcel123");

        when(commandParserService.parseDeleteCommand(inputText)).thenReturn(commandData);

        when(parcelRepository.deleteByName(commandData.parcelName())).thenThrow(new IllegalArgumentException("Ошибка при удалении посылки"));

        String result = deleteCommandHandler.execute(inputText);

        assertThat(result).isEqualTo("Ошибка при удалении посылки: Ошибка при удалении посылки");
    }
}
