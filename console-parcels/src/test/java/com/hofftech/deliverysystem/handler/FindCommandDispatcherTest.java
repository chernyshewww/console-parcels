package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.repository.ParcelRepository;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.util.FormHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCommandDispatcherTest {

    @Mock
    private ParcelRepository parcelRepository;

    @Mock
    private CommandParserService commandParserService;

    @Mock
    private ParcelService parcelService;
    @Mock
    private FormHelper formHelper;
    @Mock
    private OutputService outputService;
    @InjectMocks
    private FindCommandHandlerImpl findCommandHandler;

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
