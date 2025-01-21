package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.record.command.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CommandParserServiceTest {

    private CommandParserService commandParserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commandParserService = new CommandParserService();
    }

    @Test
    void parseLoadCommand_ShouldReturnLoadCommand_WhenCommandIsValid() {
        String inputText = "/load -u \"UserTest\" -parcels-text \"Посылка Тип 1\\nКУБ\" -trucks \"3x3\n6x2\" -type \"Одна машина - Одна посылка\" -out text";

        LoadCommand loadCommand = commandParserService.parseLoadCommand(inputText);

        assertThat(loadCommand).isNotNull();
        assertThat(loadCommand.user()).isEqualTo("UserTest");
        assertThat(loadCommand.parcelsText()).isEqualTo("Посылка Тип 1\\nКУБ");
        assertThat(loadCommand.trucksText()).isEqualTo("3x3\n6x2");
    }

    @Test
    void parseLoadCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/load -u \"UserTest\" -invalid-command";

        assertThatThrownBy(() -> commandParserService.parseLoadCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Проверьте формат команды.");
    }

    @Test
    void parseCreateCommand_ShouldReturnCreateCommand_WhenCommandIsValid() {
        String inputText = "/create -name \"NewParcel\" -form \"Square\" -symbol \"X\"";

        CreateCommand createCommand = commandParserService.parseCreateCommand(inputText);

        assertThat(createCommand).isNotNull();
        assertThat(createCommand.name()).isEqualTo("NewParcel");
        assertThat(createCommand.form()).isEqualTo("Square");
        assertThat(createCommand.symbol()).isEqualTo('X');
    }

    @Test
    void parseCreateCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/create -name \"NewParcel\" -form \"Square\"";

        assertThatThrownBy(() -> commandParserService.parseCreateCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Пожалуйста, укажите все параметры в правильном формате:");
    }

    @Test
    void parseFindCommand_ShouldReturnFindCommand_WhenCommandIsValid() {
        String inputText = "/find \"Parcel1\"";

        FindCommand findCommand = commandParserService.parseFindCommand(inputText);

        assertThat(findCommand).isNotNull();
        assertThat(findCommand.parcelName()).isEqualTo("Parcel1");
    }

    @Test
    void parseFindCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/find";

        assertThatThrownBy(() -> commandParserService.parseFindCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Пожалуйста, укажите имя посылки для поиска.");
    }

    @Test
    void parseEditCommand_ShouldReturnEditCommand_WhenCommandIsValid() {
        String inputText = "/edit -id \"123\" -name \"Parcel123\" -form \"Rectangular\" -symbol \"O\"";

        EditCommand editCommand = commandParserService.parseEditCommand(inputText);

        assertThat(editCommand).isNotNull();
        assertThat(editCommand.id()).isEqualTo("123");
        assertThat(editCommand.newName()).isEqualTo("Parcel123");
        assertThat(editCommand.newForm()).isEqualTo("Rectangular");
        assertThat(editCommand.newSymbol()).isEqualTo('O');
    }

    @Test
    void parseEditCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/edit -id \"123\" -name \"Parcel123\" -form \"Rectangular\"";

        assertThatThrownBy(() -> commandParserService.parseEditCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Укажите все параметры команды в правильном формате:");
    }

    @Test
    void parseDeleteCommand_ShouldReturnDeleteCommand_WhenCommandIsValid() {
        String inputText = "/delete \"Parcel123\"";

        DeleteCommand deleteCommand = commandParserService.parseDeleteCommand(inputText);

        assertThat(deleteCommand).isNotNull();
        assertThat(deleteCommand.parcelName()).isEqualTo("Parcel123");
    }

    @Test
    void parseDeleteCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/delete";

        assertThatThrownBy(() -> commandParserService.parseDeleteCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Пожалуйста, укажите имя посылки для удаления.");
    }

    @Test
    void parseUnloadCommand_ShouldReturnUnloadCommand_WhenCommandIsValid() {
        String inputText = "/unload -u \"UserTest\" -infile \"trucks.json\" -outfile \"parcels.csv\" --withcount";

        UnloadCommand unloadCommand = commandParserService.parseUnloadCommand(inputText);

        assertThat(unloadCommand).isNotNull();
        assertThat(unloadCommand.user()).isEqualTo("UserTest");
        assertThat(unloadCommand.inputFileName()).isEqualTo("trucks.json");
        assertThat(unloadCommand.withCount()).isTrue();
    }

    @Test
    void parseUnloadCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/unload -u \"UserTest\" -infile \"trucks.json\"";

        assertThatThrownBy(() -> commandParserService.parseUnloadCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Проверьте формат команды.");
    }

    @Test
    void parseBillingCommand_ShouldReturnBillingCommand_WhenCommandIsValid() {
        String inputText = "/billing -u \"user@example.com\" -from \"01.01.2023\" -to \"31.12.2023\"";

        BillingCommand billingCommand = commandParserService.parseBillingCommand(inputText);

        assertThat(billingCommand).isNotNull();
        assertThat(billingCommand.user()).isEqualTo("user@example.com");
        assertThat(billingCommand.fromDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(billingCommand.toDate()).isEqualTo(LocalDate.of(2023, 12, 31));
    }

    @Test
    void parseBillingCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/billing -u \"user@example.com\" -from \"01-01-2023\" -to \"31-12-2023\"";

        assertThatThrownBy(() -> commandParserService.parseBillingCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("""
        Ошибка! Проверьте формат команды.
        Пример: billing -u "alex@adlogistic.ru" -from "11.01.2025" -to 12.01.2025
        """);
    }
}
