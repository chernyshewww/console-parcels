package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.command.CreateCommand;
import com.hofftech.deliverysystem.command.DeleteCommand;
import com.hofftech.deliverysystem.command.EditCommand;
import com.hofftech.deliverysystem.command.FindCommand;
import com.hofftech.deliverysystem.command.LoadCommand;
import com.hofftech.deliverysystem.command.UnloadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class for parsing various commands from input text.
 * This class parses commands such as /load, /create, /find, /edit, /delete, and /unload.
 */
public class CommandParserService {

    /**
     * Parses a "/load" command from the provided input text.
     *
     * @param text The command text to parse.
     * @return A LoadCommand object with parsed details.
     * @throws InvalidCommandException If the command format is incorrect.
     */
    public LoadCommand parseLoadCommand(String text) throws InvalidCommandException {
        Pattern pattern = Pattern.compile(
                "/load (-parcels-text \"([^\"]+)\"|-parcels-file \"([^\"]+)\") -trucks \"([^\"]+)\" -type \"([^\"]+)\" -out (text|json-file)(?: -out-filename \"([^\"]+)\")?");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException(""" 
                Ошибка! Проверьте формат команды.
                Примеры:
                /load -parcels-text "Посылка Тип 1\\nКУБ" -trucks "3x3\\n6x2" -type "Одна машина - Одна посылка" -out text
                /load -parcels-file "parcels.csv" -trucks "3x3\\n6x2" -type "Одна машина - Одна посылка" -out json-file -out-filename "trucks.json"
                """);
        }

        return new LoadCommand(
                matcher.group(2),
                matcher.group(3),
                matcher.group(4),
                matcher.group(5),
                matcher.group(6),
                matcher.group(7)
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
                matcher.group(1),
                matcher.group(2),
                matcher.group(3).charAt(0)
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

        return new FindCommand(matcher.group(1));
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
                matcher.group(1),
                matcher.group(2),
                matcher.group(3),
                matcher.group(4).charAt(0)
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

        return new DeleteCommand(matcher.group(1));
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
                "/unload -infile \"([^\"]+)\" -outfile \"([^\"]+)\"( --withcount)?");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            throw new InvalidCommandException("""
            Ошибка! Проверьте формат команды.
            Пример: /unload -infile "trucks.json" -outfile "parcels.csv" --withcount
        """);
        }

        return new UnloadCommand(
                matcher.group(1),
                matcher.group(2),
                matcher.group(3) != null
        );
    }
}
