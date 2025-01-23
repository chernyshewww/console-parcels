package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.constants.Constant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HelpCommandDispatcherImplTest {

    @InjectMocks
    private HelpCommandHandlerImpl helpCommandHandler;

    @Test
    @DisplayName("Должен возвращать корректный текст справки")
    void handle_ShouldReturnHelpText() {
        String expectedHelpText = Constant.HELP_TEXT.getValue().toString();

        assertThat(helpCommandHandler.handle("")).isEqualTo(expectedHelpText);
    }
}
