package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.model.entity.ParcelEntity;
import com.hofftech.deliverysystem.model.record.command.EditCommand;
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
class EditCommandDispatcherTest {

    @Mock
    private ParcelRepository parcelRepository;

    @Mock
    private CommandParserService commandParserService;

    @InjectMocks
    private EditCommandHandlerImpl editCommandHandler;

    @Test
    @DisplayName("Должен вернуть ошибку при некорректном формате команды")
    void handle_ShouldReturnErrorMessage_WhenCommandIsInvalid() {
        String inputText = "/edit";

        when(commandParserService.parseEditCommand(inputText))
                .thenThrow(new InvalidCommandException("Неверный формат команды"));

        assertThatThrownBy(() -> editCommandHandler.handle(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessage("Неверный формат команды");
    }
}


