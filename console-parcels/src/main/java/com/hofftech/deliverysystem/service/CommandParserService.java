package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.record.command.BillingCommand;
import com.hofftech.deliverysystem.model.record.command.CreateCommand;
import com.hofftech.deliverysystem.model.record.command.DeleteCommand;
import com.hofftech.deliverysystem.model.record.command.EditCommand;
import com.hofftech.deliverysystem.model.record.command.FindCommand;
import com.hofftech.deliverysystem.model.record.command.LoadCommand;
import com.hofftech.deliverysystem.model.record.command.UnloadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommandParserService {

    private static final int LOAD_USER_GROUP = 1;
    private static final int LOAD_PARCELS_TEXT_GROUP = 2;
    private static final int LOAD_TRUCKS_GROUP = 3;
    private static final int LOAD_TYPE_GROUP = 4;
    private static final int LOAD_OUT_GROUP = 5;
    private static final int LOAD_OUT_FILENAME_GROUP = 6;

    private static final int CREATE_NAME_GROUP = 1;
    private static final int CREATE_FORM_GROUP = 2;
    private static final int CREATE_SYMBOL_GROUP = 3;

    private static final int FIND_NAME_GROUP = 1;

    private static final int EDIT_ID_GROUP = 1;
    private static final int EDIT_NAME_GROUP = 2;
    private static final int EDIT_FORM_GROUP = 3;
    private static final int EDIT_SYMBOL_GROUP = 4;

    private static final int DELETE_NAME_GROUP = 1;

    private static final int UNLOAD_USER_GROUP = 1;
    private static final int UNLOAD_OUTFILE_GROUP = 2;
    private static final int UNLOAD_WITHCOUNT_GROUP = 3;

    private static final String LOAD_COMMAND_FORMAT = "/load -u \"([^\"]+)\"(?: -parcels \"([^\"]+)\")? -trucks \"([^\"]+)\" -type \"([^\"]+)\" -out (text|json-file)(?: -out-filename \"([^\"]+)\")?";
    private static final String CREATE_COMMAND_FORMAT = "-name \"([^\"]+)\" -form \"([^\"]+)\" -symbol \"([^\"]+)\"";
    private static final String FIND_COMMAND_FORMAT = "/find \"([^\"]+)\"";
    private static final String EDIT_COMMAND_FORMAT = "-id\\s+\"([^\"]+)\"\\s*-name\\s+\"([^\"]+)\"\\s*-form\\s+\"([^\"]+)\"\\s*-symbol\\s+\"(.)\"";
    private static final String DELETE_COMMAND_FORMAT = "/delete \"([^\"]+)\"";
    private static final String UNLOAD_COMMAND_FORMAT = "/unload -u \"([^\"]+)\" -outfile \"([^\"]+)\"( --withcount)?";
    private static final String BILLING_COMMAND_FORMAT = "/billing -u \"([^\\s]+)\" -from \"(\\d{2}\\.\\d{2}\\.\\d{4})\" -to \"(\\d{2}\\.\\d{2}\\.\\d{4})\"";

    private static final String DATE_FORMAT= "dd.MM.yyyy";
    private static final String ERROR_INVALID_LOAD_COMMAND = """
        Ошибка! Проверьте формат команды.
        Примеры:
        /load -u "UserTest" -parcels "КУБ22\nTestParcel" -trucks "3x3\n6x2" -type "Одна машина - Одна посылка" -out text
        /load -u "UserTest" -parcels "All" -trucks "3x3\n6x2" -type "Одна машина - Одна посылка" -out json-file -out-filename "trucks.json"
        """;

    private static final String ERROR_INVALID_CREATE_COMMAND = """
        Ошибка! Пожалуйста, укажите все параметры в правильном формате:
        /create -name "Название посылки" -form "форма посылки" -symbol "символ"
        """;

    private static final String ERROR_INVALID_FIND_COMMAND = """
        Ошибка! Пожалуйста, укажите имя посылки для поиска.
        Пример: /find "Название посылки"
        """;

    private static final String ERROR_INVALID_EDIT_COMMAND = """
        Ошибка! Укажите все параметры команды в правильном формате:
        /edit -id "старое имя" -name "новое имя" -form "новая форма" -symbol "новый символ"
        """;

    private static final String ERROR_INVALID_DELETE_COMMAND = """
        Ошибка! Пожалуйста, укажите имя посылки для удаления.
        Пример: /delete "Название посылки"
        """;

    private static final String ERROR_INVALID_UNLOAD_COMMAND = """
        Ошибка! Проверьте формат команды.
        Пример: /unload -u "testUser" --outfile "parcels.csv" --withcount
        """;

    private static final String ERROR_INVALID_BILLING_COMMAND = """
        Ошибка! Проверьте формат команды.
        Пример: billing -u "alex@adlogistic.ru" -from "11.01.2025" -to 12.01.2025
        """;

    private static final String ERROR_INVALID_DATE_FORMAT = """
        Ошибка! Убедитесь, что даты указаны в формате ДД.ММ.ГГГГ.
        Пример: billing -u alex@adlogistic.ru -from 11.01.2025 -to 12.01.2025
        """;

    public LoadCommand parseLoadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(LOAD_COMMAND_FORMAT);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_INVALID_LOAD_COMMAND);
        }

        String user = matcher.group(LOAD_USER_GROUP);
        String parcels = matcher.group(LOAD_PARCELS_TEXT_GROUP);
        String trucks = matcher.group(LOAD_TRUCKS_GROUP);
        String type = matcher.group(LOAD_TYPE_GROUP);
        String out = matcher.group(LOAD_OUT_GROUP);
        String outFilename = matcher.group(LOAD_OUT_FILENAME_GROUP);

        return new LoadCommand(
                user,
                parcels,
                trucks,
                type,
                out,
                outFilename
        );
    }

    public CreateCommand parseCreateCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(CREATE_COMMAND_FORMAT);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_INVALID_CREATE_COMMAND);
        }

        return new CreateCommand(
                matcher.group(CREATE_NAME_GROUP),
                matcher.group(CREATE_FORM_GROUP),
                matcher.group(CREATE_SYMBOL_GROUP).charAt(0)
        );
    }

    public FindCommand parseFindCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(FIND_COMMAND_FORMAT);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_INVALID_FIND_COMMAND);
        }

        return new FindCommand(matcher.group(FIND_NAME_GROUP));
    }

    public EditCommand parseEditCommand(String text) throws InvalidCommandException {
        text = text.replace("“", "\"").replace("”", "\"");

        Pattern pattern = Pattern.compile(EDIT_COMMAND_FORMAT);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_INVALID_EDIT_COMMAND);
        }

        return new EditCommand(
                matcher.group(EDIT_ID_GROUP),
                matcher.group(EDIT_NAME_GROUP),
                matcher.group(EDIT_FORM_GROUP),
                matcher.group(EDIT_SYMBOL_GROUP).charAt(0)
        );
    }

    public DeleteCommand parseDeleteCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(DELETE_COMMAND_FORMAT);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_INVALID_DELETE_COMMAND);
        }

        return new DeleteCommand(matcher.group(DELETE_NAME_GROUP));
    }

    public UnloadCommand parseUnloadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(UNLOAD_COMMAND_FORMAT);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_INVALID_UNLOAD_COMMAND);
        }

        return new UnloadCommand(
                matcher.group(UNLOAD_USER_GROUP),
                matcher.group(UNLOAD_OUTFILE_GROUP),
                matcher.group(UNLOAD_WITHCOUNT_GROUP) != null
        );
    }

    public BillingCommand parseBillingCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(BILLING_COMMAND_FORMAT);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_INVALID_BILLING_COMMAND);
        }

        try {
            String user = matcher.group(1);
            LocalDate fromDate = parseDate(matcher.group(2));
            LocalDate toDate = parseDate(matcher.group(3));

            return new BillingCommand(user, fromDate, toDate);
        } catch (DateTimeParseException e) {
            throw new InvalidCommandException(ERROR_INVALID_DATE_FORMAT);
        }
    }

    private LocalDate parseDate(String dateText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(dateText, formatter);
    }
}
