package com.hofftech.deliverysystem.handler;

import com.hofftech.deliverysystem.constants.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HelpCommandHandlerImplTest {

    private HelpCommandHandlerImpl helpCommandHandler;

    @BeforeEach
    void setUp() {
        helpCommandHandler = new HelpCommandHandlerImpl();
    }

    @Test
    void execute_ShouldReturnHelpText() {
        String expectedHelpText = Constant.HELP_TEXT.getValue().toString();

        assertThat(helpCommandHandler.execute("")).isEqualTo(expectedHelpText);
    }
}
