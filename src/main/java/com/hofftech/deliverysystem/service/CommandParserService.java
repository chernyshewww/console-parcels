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

/**
 * Service class for parsing various commands from input text.
 * This class parses commands such as /load, /create, /find, /edit, /delete, and /unload.
 */
@Service
public class CommandParserService {

    private static final int LOAD_USER_GROUP = 1;
    private static final int LOAD_PARCELS_TEXT_GROUP = 2;
    private static final int LOAD_PARCELS_FILE_GROUP = 3;
    private static final int LOAD_TRUCKS_GROUP = 4;
    private static final int LOAD_TYPE_GROUP = 5;
    private static final int LOAD_OUT_GROUP = 6;
    private static final int LOAD_OUT_FILENAME_GROUP = 7;

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
    private static final int UNLOAD_INFILE_GROUP = 2;
    private static final int UNLOAD_OUTFILE_GROUP = 3;
    private static final int UNLOAD_WITHCOUNT_GROUP = 4;

    /**
     * Parses a "/load" command from the provided input text.
     *
     * @param text The command text to parse.
     * @return A LoadCommand object with parsed details.
     * @throws InvalidCommandException If the command format is incorrect.
     */
    public LoadCommand parseLoadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(
                "/load -u \"([^\"]+)\"" +
                        "(?: -parcels-text \"([^\"]+)\")?" +
                        "(?: -parcels-file \"([^\"]+)\")?" +
                        " -trucks \"([^\"]+)\"" +
                        " -type \"([^\"]+)\"" +
                        " -out (text|json-file)" +
                        "(?: -out-filename \"([^\"]+)\")?");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException("""
                Ошибка! Проверьте формат команды.
                Примеры:
                /load -u "UserTest" -parcels-text "Посылка Тип 1\\nКУБ" -trucks "3x3\\n6x2" -type "Одна машина - Одна посылка" -out text
                /load -u "UserTest" -parcels-file "parcels.csv" -trucks "3x3\\n6x2" -type "Одна машина - Одна посылка" -out json-file -out-filename "trucks.json"
                """);
        }

        String user = matcher.group(LOAD_USER_GROUP);
        String parcelsText = matcher.group(LOAD_PARCELS_TEXT_GROUP);
        String parcelsFile = matcher.group(LOAD_PARCELS_FILE_GROUP);
        String trucks = matcher.group(LOAD_TRUCKS_GROUP);
        String type = matcher.group(LOAD_TYPE_GROUP);
        String out = matcher.group(LOAD_OUT_GROUP);
        String outFilename = matcher.group(LOAD_OUT_FILENAME_GROUP);

        return new LoadCommand(
                user,
                parcelsText,
                parcelsFile,
                trucks,
                type,
                out,
                outFilename
        );
    }

    /**
     * Parses a "/create" command from the provided input text.
     *
     * @param text The command text to parse.
     * @return A CreateCommand object with parsed details.
     * @throws InvalidCommandException If the command format is incorrect.
     */
    public CreateCommand parseCreateCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile("-name \"([^\"]+)\" -form \"([^\"]+)\" -symbol \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(""" 
                Ошибка! Пожалуйста, укажите все параметры в правильном формате:
                /create -name "Название посылки" -form "форма посылки" -symbol "символ"
                """);
        }

        return new CreateCommand(
                matcher.group(CREATE_NAME_GROUP),
                matcher.group(CREATE_FORM_GROUP),
                matcher.group(CREATE_SYMBOL_GROUP).charAt(0)
        );
    }

    /**
     * Parses a "/find" command from the provided input text.
     *
     * @param text The command text to parse.
     * @return A FindCommand object with parsed details.
     * @throws InvalidCommandException If the command format is incorrect.
     */
    public FindCommand parseFindCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile("/find \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(""" 
                Ошибка! Пожалуйста, укажите имя посылки для поиска.
                Пример: /find "Название посылки"
                """);
        }

        return new FindCommand(matcher.group(FIND_NAME_GROUP));
    }

    /**
     * Parses an "/edit" command from the provided input text.
     *
     * @param text The command text to parse.
     * @return An EditCommand object with parsed details.
     * @throws InvalidCommandException If the command format is incorrect.
     */
    public EditCommand parseEditCommand(String text) throws InvalidCommandException {
        text = text.replace("“", "\"").replace("”", "\"");

        Pattern pattern = Pattern.compile(
                "-id\\s+\"([^\"]+)\"\\s*" +
                        "-name\\s+\"([^\"]+)\"\\s*" +
                        "-form\\s+\"([^\"]+)\"\\s*" +
                        "-symbol\\s+\"(.)\"\\s*"
        );

        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(""" 
            Ошибка! Укажите все параметры команды в правильном формате:
            /edit -id "старое имя" -name "новое имя" -form "новая форма" -symbol "новый символ"
            """);
        }

        return new EditCommand(
                matcher.group(EDIT_ID_GROUP),
                matcher.group(EDIT_NAME_GROUP),
                matcher.group(EDIT_FORM_GROUP),
                matcher.group(EDIT_SYMBOL_GROUP).charAt(0)
        );
    }

    /**
     * Parses a "/delete" command from the provided input text.
     *
     * @param text The command text to parse.
     * @return A DeleteCommand object with parsed details.
     * @throws InvalidCommandException If the command format is incorrect.
     */
    public DeleteCommand parseDeleteCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile("/delete \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(""" 
            Ошибка! Пожалуйста, укажите имя посылки для удаления.
            Пример: /delete "Название посылки"
            """);
        }

        return new DeleteCommand(matcher.group(DELETE_NAME_GROUP));
    }

    /**
     * Parses a "/unload" command from the provided input text.
     *
     * @param text The command text to parse.
     * @return An UnloadCommand object with parsed details.
     * @throws InvalidCommandException If the command format is incorrect.
     */
    public UnloadCommand parseUnloadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(
                "/unload -u \"([^\"]+)\" -infile \"([^\"]+)\" -outfile \"([^\"]+)\"( --withcount)?");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException("""
                Ошибка! Проверьте формат команды.
                Пример: /unload -u "testUser" -infile "trucks.json" -outfile "parcels.csv" --withcount
                """);
        }

        return new UnloadCommand(
                matcher.group(UNLOAD_USER_GROUP),
                matcher.group(UNLOAD_INFILE_GROUP),
                matcher.group(UNLOAD_OUTFILE_GROUP),
                matcher.group(UNLOAD_WITHCOUNT_GROUP) != null
        );
    }

    public BillingCommand parseBillingCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(
                "/billing -u \"([^\\s]+)\" -from \"(\\d{2}\\.\\d{2}\\.\\d{4})\" -to \"(\\d{2}\\.\\d{2}\\.\\d{4})\"");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException("""
        Ошибка! Проверьте формат команды.
        Пример: billing -u "alex@adlogistic.ru" -from "11.01.2025" -to 12.01.2025
        """);
        }

        try {
            String user = matcher.group(1);
            LocalDate fromDate = parseDate(matcher.group(2));
            LocalDate toDate = parseDate(matcher.group(3));

            return new BillingCommand(user, fromDate, toDate);
        } catch (DateTimeParseException e) {
            throw new InvalidCommandException("""
        Ошибка! Убедитесь, что даты указаны в формате ДД.ММ.ГГГГ.
        Пример: billing -u alex@adlogistic.ru -from 11.01.2025 -to 12.01.2025
        """);
        }
    }

    private LocalDate parseDate(String dateText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(dateText, formatter);
    }
}
