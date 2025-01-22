package com.hofftech.deliverysystem.command;

import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandHandlerTest {

    @Mock
    private CommandFactory commandFactory;

    @Mock
    private Command command;

    @InjectMocks
    private CommandHandler commandHandler;

    @Test
    @DisplayName("Должен вернуть результат выполнения команды, когда команда валидна")
    void shouldReturnCommandExecutionResult_whenCommandIsValid() {
        String commandText = "test";
        String inputText = "test argument";
        String expectedResponse = "Command executed";

        when(commandFactory.getCommand(commandText)).thenReturn(command);
        when(command.execute(inputText)).thenReturn(expectedResponse);

        String result = commandHandler.handleCommand(inputText);

        assertThat(result).isEqualTo(expectedResponse);
        verify(commandFactory).getCommand(commandText);
        verify(command).execute(inputText);
    }

    @Test
    @DisplayName("Должен вернуть сообщение об ошибке, когда команда невалидна")
    void shouldReturnErrorMessage_whenCommandIsInvalid() {
        String commandText = "invalid";
        String inputText = "invalid argument";
        String expectedError = "Invalid command";

        when(commandFactory.getCommand(commandText)).thenThrow(new InvalidCommandException(expectedError));

        String result = commandHandler.handleCommand(inputText);

        assertThat(result).isEqualTo(expectedError);
        verify(commandFactory).getCommand(commandText);
        verifyNoInteractions(command);
    }
}
