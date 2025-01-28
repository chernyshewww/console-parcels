package com.hofftech.deliverysystem.telegram.service;

import com.hofftech.deliverysystem.telegram.client.DeliverySystemRestClient;
import com.hofftech.deliverysystem.telegram.exception.InvalidCommandException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DeliverySystemService {

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
    private static final int UNLOAD_OUTFILE_GROUP = 2;
    private static final int UNLOAD_WITHCOUNT_GROUP = 3;
    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private static final String ERROR_LOAD_COMMAND_FORMAT = "Ошибка! Проверьте формат команды.\nПримеры:\n" +
            "/load -u \"UserTest\" -parcels \"КУБ22\nTestParcel\" -trucks \"3x3\n6x2\" -type \"Одна машина - Одна посылка\" -out text\n" +
            "/load -u \"UserTest\" -parcels \"All\" -trucks \"3x3\n6x2\" -type \"Одна машина - Одна посылка\" -out json-file -out-filename \"trucks.json\"";

    private static final String ERROR_CREATE_COMMAND_FORMAT = "Ошибка! Пожалуйста, укажите все параметры в правильном формате:\n" +
            "/create -name \"Название посылки\" -form \"форма посылки\" -symbol \"символ\"";

    private static final String ERROR_FIND_COMMAND_FORMAT = "Ошибка! Пожалуйста, укажите имя посылки для поиска.\nПример: /find \"Название посылки\"";

    private static final String ERROR_EDIT_COMMAND_FORMAT = "Ошибка! Укажите все параметры команды в правильном формате:\n" +
            "/edit -id \"старое имя\" -name \"новое имя\" -form \"новая форма\" -symbol \"новый символ\"";

    private static final String ERROR_DELETE_COMMAND_FORMAT = "Ошибка! Пожалуйста, укажите имя посылки для удаления.\nПример: /delete \"Название посылки\"";

    private static final String ERROR_UNLOAD_COMMAND_FORMAT = "Ошибка! Проверьте формат команды.\nПример: /unload -u \"testUser\" -infile \"trucks.json\" -outfile \"parcels.csv\" --withcount";

    private static final String ERROR_BILLING_COMMAND_FORMAT = "Ошибка! Проверьте формат команды.\nПример: billing -u \"alex@adlogistic.ru\" -from \"11.01.2025\" -to 12.01.2025";

    private static final String ERROR_DATE_FORMAT = "Ошибка! Убедитесь, что даты указаны в формате ДД.ММ.ГГГГ.\nПример: billing -u alex@adlogistic.ru -from 11.01.2025 -to 12.01.2025";

    private static final String LOAD_COMMAND_PATTERN = "/load -u \"([^\"]+)\"" +
            "(?: -parcels \"([^\"]+)\")?" +
            " -trucks \"([^\"]+)\"" +
            " -type \"([^\"]+)\"" +
            " -out (text|json-file)" +
            "(?: -out-filename \"([^\"]+)\")?";

    private static final String CREATE_COMMAND_PATTERN = "-name \"([^\"]+)\" -form \"([^\"]+)\" -symbol \"([^\"]+)\"";

    private static final String FIND_COMMAND_PATTERN = "/find \"([^\"]+)\"";

    private static final String EDIT_COMMAND_PATTERN = "-id\\s+\"([^\"]+)\"\\s*" +
            "-name\\s+\"([^\"]+)\"\\s*" +
            "-form\\s+\"([^\"]+)\"\\s*" +
            "-symbol\\s+\"(.)\"\\s*";

    private static final String DELETE_COMMAND_PATTERN = "/delete \"([^\"]+)\"";

    private static final String UNLOAD_COMMAND_PATTERN = "/unload -u \"([^\"]+)\" -outfile \"([^\"]+)\"( --withcount)?";

    private static final String BILLING_COMMAND_PATTERN = "/billing -u \"([^\\s]+)\" -from \"(\\d{2}\\.\\d{2}\\.\\d{4})\" -to \"(\\d{2}\\.\\d{2}\\.\\d{4})\"";

    private final DeliverySystemRestClient deliverySystemRestClient;

    public String sendLoadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(LOAD_COMMAND_PATTERN);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_LOAD_COMMAND_FORMAT);
        }

        String user = matcher.group(LOAD_USER_GROUP);
        String parcels = matcher.group(LOAD_PARCELS_TEXT_GROUP);
        String trucks = matcher.group(LOAD_TRUCKS_GROUP);
        String type = matcher.group(LOAD_TYPE_GROUP);
        String out = matcher.group(LOAD_OUT_GROUP);
        String outFilename = matcher.group(LOAD_OUT_FILENAME_GROUP);

        return deliverySystemRestClient.load(
                user,
                parcels,
                trucks,
                type,
                out,
                outFilename
        );
    }

    public String sendCreateCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(CREATE_COMMAND_PATTERN);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_CREATE_COMMAND_FORMAT);
        }

        return deliverySystemRestClient.createParcel(
                matcher.group(CREATE_NAME_GROUP),
                matcher.group(CREATE_FORM_GROUP),
                matcher.group(CREATE_SYMBOL_GROUP).charAt(0)
        );
    }

    public String sendFindCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(FIND_COMMAND_PATTERN);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_FIND_COMMAND_FORMAT);
        }

        return deliverySystemRestClient.findParcel(matcher.group(FIND_NAME_GROUP));
    }

    public String sendEditCommand(String text) throws InvalidCommandException {
        text = text.replace("“", "\"").replace("”", "\"");

        Pattern pattern = Pattern.compile(EDIT_COMMAND_PATTERN);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_EDIT_COMMAND_FORMAT);
        }

        return deliverySystemRestClient.edit(
                matcher.group(EDIT_ID_GROUP),
                matcher.group(EDIT_NAME_GROUP),
                matcher.group(EDIT_FORM_GROUP),
                matcher.group(EDIT_SYMBOL_GROUP).charAt(0)
        );
    }

    public String sendDeleteCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(DELETE_COMMAND_PATTERN);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_DELETE_COMMAND_FORMAT);
        }
        return deliverySystemRestClient.deleteParcel(matcher.group(DELETE_NAME_GROUP));
    }

    public String sendUnloadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(UNLOAD_COMMAND_PATTERN);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_UNLOAD_COMMAND_FORMAT);
        }

        return deliverySystemRestClient.unload(
                matcher.group(UNLOAD_USER_GROUP),
                matcher.group(UNLOAD_OUTFILE_GROUP),
                matcher.group(UNLOAD_WITHCOUNT_GROUP) != null
        );
    }

    public String sendBillingCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(BILLING_COMMAND_PATTERN);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(ERROR_BILLING_COMMAND_FORMAT);
        }

        try {
            String user = matcher.group(1);
            LocalDate fromDate = parseDate(matcher.group(2));
            LocalDate toDate = parseDate(matcher.group(3));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            String formattedFromDate = fromDate.format(formatter);
            String formattedToDate = toDate.format(formatter);

            return deliverySystemRestClient.billing(user, formattedFromDate, formattedToDate);
        } catch (DateTimeParseException e) {
            throw new InvalidCommandException(ERROR_DATE_FORMAT);
        }
    }

    private LocalDate parseDate(String dateText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(dateText, formatter);
    }
}
