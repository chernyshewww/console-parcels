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

    private final DeliverySystemRestClient deliverySystemRestClient;

    public String sendLoadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(
                "/load -u \"([^\"]+)\"" +
                        "(?: -parcels \"([^\"]+)\")?" +  // Single 'parcels' argument for both cases (All or text)
                        " -trucks \"([^\"]+)\"" +
                        " -type \"([^\"]+)\"" +
                        " -out (text|json-file)" +
                        "(?: -out-filename \"([^\"]+)\")?");

        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException("""
                    Ошибка! Проверьте формат команды.
                    Примеры:
                    /load -u "UserTest" -parcels "КУБ22\nTestParcel" -trucks "3x3\n6x2" -type "Одна машина - Одна посылка" -out text
                    /load -u "UserTest" -parcels "All" -trucks "3x3\n6x2" -type "Одна машина - Одна посылка" -out json-file -out-filename "trucks.json"
                    """);
        }

        String user = matcher.group(1);
        String parcels = matcher.group(2);  // This will either be "All" or a description like "КУБ22\nTestParcel"
        String trucks = matcher.group(3);
        String type = matcher.group(4);
        String out = matcher.group(5);
        String outFilename = matcher.group(6);

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
        Pattern pattern = Pattern.compile("-name \"([^\"]+)\" -form \"([^\"]+)\" -symbol \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(""" 
                    Ошибка! Пожалуйста, укажите все параметры в правильном формате:
                    /create -name "Название посылки" -form "форма посылки" -symbol "символ"
                    """);
        }

        return deliverySystemRestClient.createParcel(
                matcher.group(CREATE_NAME_GROUP),
                matcher.group(CREATE_FORM_GROUP),
                matcher.group(CREATE_SYMBOL_GROUP).charAt(0)
        );
    }


    public String sendFindCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile("/find \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(""" 
                    Ошибка! Пожалуйста, укажите имя посылки для поиска.
                    Пример: /find "Название посылки"
                    """);
        }

        return deliverySystemRestClient.findParcel(matcher.group(FIND_NAME_GROUP));
    }

    public String sendEditCommand(String text) throws InvalidCommandException {
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

        return deliverySystemRestClient.edit(
                matcher.group(EDIT_ID_GROUP),
                matcher.group(EDIT_NAME_GROUP),
                matcher.group(EDIT_FORM_GROUP),
                matcher.group(EDIT_SYMBOL_GROUP).charAt(0)
        );
    }


    public String sendDeleteCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile("/delete \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(""" 
                    Ошибка! Пожалуйста, укажите имя посылки для удаления.
                    Пример: /delete "Название посылки"
                    """);
        }
        return deliverySystemRestClient.deleteParcel(matcher.group(DELETE_NAME_GROUP));
    }

    public String sendUnloadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(
                "/unload -u \"([^\"]+)\" -outfile \"([^\"]+)\"( --withcount)?");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException("""
                    Ошибка! Проверьте формат команды.
                    Пример: /unload -u "testUser" -infile "trucks.json" -outfile "parcels.csv" --withcount
                    """);
        }

        return deliverySystemRestClient.unload(
                matcher.group(UNLOAD_USER_GROUP),
                matcher.group(UNLOAD_OUTFILE_GROUP),
                matcher.group(UNLOAD_WITHCOUNT_GROUP) != null
        );
    }

    public String sendBillingCommand(String text) throws InvalidCommandException {
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedFromDate = fromDate.format(formatter);
            String formattedToDate = toDate.format(formatter);

            return  deliverySystemRestClient.billing(user, formattedFromDate, formattedToDate);
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
