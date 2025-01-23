package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.record.command.BillingCommand;
import com.hofftech.deliverysystem.model.record.command.CreateCommand;
import com.hofftech.deliverysystem.model.record.command.DeleteCommand;
import com.hofftech.deliverysystem.model.record.command.EditCommand;
import com.hofftech.deliverysystem.model.record.command.FindCommand;
import com.hofftech.deliverysystem.model.record.command.LoadCommand;
import com.hofftech.deliverysystem.model.record.command.UnloadCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CommandParserServiceTest {

    @InjectMocks
    private CommandParserService commandParserService;

    @Test
    @DisplayName("Должен вернуть команду погрузки при правильном формате команды")
    void parseLoadCommand_ShouldReturnLoadCommand_WhenCommandIsValid() {
        String inputText = "/load -u \"UserTest\" -parcels-text \"Посылка Тип 1\\nКУБ\" -trucks \"3x3\n6x2\" -type \"Одна машина - Одна посылка\" -out text";

        LoadCommand loadCommand = commandParserService.parseLoadCommand(inputText);

        assertThat(loadCommand).isNotNull();
        assertThat(loadCommand.user()).isEqualTo("UserTest");
        assertThat(loadCommand.parcelsText()).isEqualTo("Посылка Тип 1\\nКУБ");
        assertThat(loadCommand.trucksText()).isEqualTo("3x3\n6x2");
    }

    @Test
    @DisplayName("Должен выбросить исключение при неправильной команде погрузки")
    void parseLoadCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/load -u \"UserTest\" -invalid-command";

        assertThatThrownBy(() -> commandParserService.parseLoadCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Проверьте формат команды.");
    }

    @Test
    @DisplayName("Должен вернуть команду создания при правильном формате команды")
    void parseCreateCommand_ShouldReturnCreateCommand_WhenCommandIsValid() {
        String inputText = "/create -name \"NewParcel\" -form \"Square\" -symbol \"X\"";

        CreateCommand createCommand = commandParserService.parseCreateCommand(inputText);

        assertThat(createCommand).isNotNull();
        assertThat(createCommand.name()).isEqualTo("NewParcel");
        assertThat(createCommand.form()).isEqualTo("Square");
        assertThat(createCommand.symbol()).isEqualTo('X');
    }

    @Test
    @DisplayName("Должен выбросить исключение при неправильной команде создания")
    void parseCreateCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/create -name \"NewParcel\" -form \"Square\"";

        assertThatThrownBy(() -> commandParserService.parseCreateCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Пожалуйста, укажите все параметры в правильном формате:");
    }

    @Test
    @DisplayName("Должен вернуть команду поиска при правильном формате команды")
    void parseFindCommand_ShouldReturnFindCommand_WhenCommandIsValid() {
        String inputText = "/find \"Parcel1\"";

        FindCommand findCommand = commandParserService.parseFindCommand(inputText);

        assertThat(findCommand).isNotNull();
        assertThat(findCommand.parcelName()).isEqualTo("Parcel1");
    }

    @Test
    @DisplayName("Должен выбросить исключение при неправильной команде поиска")
    void parseFindCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/find";

        assertThatThrownBy(() -> commandParserService.parseFindCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Пожалуйста, укажите имя посылки для поиска.");
    }

    @Test
    @DisplayName("Должен вернуть команду редактирования при правильном формате команды")
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
    @DisplayName("Должен выбросить исключение при неправильной команде редактирования")
    void parseEditCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/edit -id \"123\" -name \"Parcel123\" -form \"Rectangular\"";

        assertThatThrownBy(() -> commandParserService.parseEditCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Укажите все параметры команды в правильном формате:");
    }

    @Test
    @DisplayName("Должен вернуть команду удаления при правильном формате команды")
    void parseDeleteCommand_ShouldReturnDeleteCommand_WhenCommandIsValid() {
        String inputText = "/delete \"Parcel123\"";

        DeleteCommand deleteCommand = commandParserService.parseDeleteCommand(inputText);

        assertThat(deleteCommand).isNotNull();
        assertThat(deleteCommand.parcelName()).isEqualTo("Parcel123");
    }

    @Test
    @DisplayName("Должен выбросить исключение при неправильной команде удаления")
    void parseDeleteCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/delete";

        assertThatThrownBy(() -> commandParserService.parseDeleteCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Пожалуйста, укажите имя посылки для удаления.");
    }

    @Test
    @DisplayName("Должен вернуть команду разгрузки при правильном формате команды")
    void parseUnloadCommand_ShouldReturnUnloadCommand_WhenCommandIsValid() {
        String inputText = "/unload -u \"UserTest\" -infile \"trucks.json\" -outfile \"parcels.csv\" --withcount";

        UnloadCommand unloadCommand = commandParserService.parseUnloadCommand(inputText);

        assertThat(unloadCommand).isNotNull();
        assertThat(unloadCommand.user()).isEqualTo("UserTest");
        assertThat(unloadCommand.inputFileName()).isEqualTo("trucks.json");
        assertThat(unloadCommand.withCount()).isTrue();
    }

    @Test
    @DisplayName("Должен выбросить исключение при неправильной команде разгрузки")
    void parseUnloadCommand_ShouldThrowInvalidCommandException_WhenCommandIsInvalid() {
        String inputText = "/unload -u \"UserTest\" -infile \"trucks.json\"";

        assertThatThrownBy(() -> commandParserService.parseUnloadCommand(inputText))
                .isInstanceOf(InvalidCommandException.class)
                .hasMessageContaining("Ошибка! Проверьте формат команды.");
    }

    @Test
    @DisplayName("Должен вернуть команду для выставления счета при правильном формате команды")
    void parseBillingCommand_ShouldReturnBillingCommand_WhenCommandIsValid() {
        String inputText = "/billing -u \"user@example.com\" -from \"01.01.2023\" -to \"31.12.2023\"";

        BillingCommand billingCommand = commandParserService.parseBillingCommand(inputText);

        assertThat(billingCommand).isNotNull();
        assertThat(billingCommand.user()).isEqualTo("user@example.com");
        assertThat(billingCommand.fromDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(billingCommand.toDate()).isEqualTo(LocalDate.of(2023, 12, 31));
    }

    @Test
    @DisplayName("Должен выбросить исключение при неправильной команде для выставления счета")
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
