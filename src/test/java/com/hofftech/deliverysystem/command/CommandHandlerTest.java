package com.hofftech.deliverysystem.command;

import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.service.CommandFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CommandHandlerTest {

    @Mock
    private CommandFactory commandFactory;

    @Mock
    private Command command;

    private CommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commandHandler = new CommandHandler(commandFactory);
    }

    @Test
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

