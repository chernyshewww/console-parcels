package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.command.CommandHandler;
import com.hofftech.deliverysystem.service.LoadCommandService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@AllArgsConstructor
public class DeliverySystemShellController {

    private static final String USER_SHORT_COMMAND = " -u \"";
    private final CommandHandler commandHandler;
    private final LoadCommandService loadCommandService;

    @ShellMethod(key = "create", value = "Create a new parcel. Usage: create <name> <symbol> <form>")
    public String create(
            @ShellOption(help = "Name of the parcel") String name,
            @ShellOption(help = "Form of the parcel (e.g., 'TT\\nTT')") String form,
            @ShellOption(help = "Symbol representing the parcel") char symbol) {
        String command = String.format("/create -name \"%s\" -form \"%s\" -symbol \"%c\"", name, form, symbol );
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "find", value = "Find a parcel by name. Usage: find <name>")
    public String find(
            @ShellOption(help = "Name of the parcel to find") String name) {
        String command = String.format("/find \"%s\"", name);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "edit", value = "Edit a parcel. Usage: edit <id> <newName> <newForm> <newSymbol>")
    public String edit(
            @ShellOption(help = "ID of the parcel to edit") String id,
            @ShellOption(help = "New name for the parcel") String name,
            @ShellOption(help = "New form for the parcel (e.g., 'UU\\nUU')") String form,
            @ShellOption(help = "New symbol for the parcel") char symbol) {
        String command = String.format("/edit -id \"%s\" -name \"%s\" -form \"%s\" -symbol \"%c\"", id, name, form, symbol);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "delete", value = "Delete a parcel by name. Usage: delete <name>")
    public String delete(
            @ShellOption(help = "Name of the parcel to delete") String name) {
        String command = String.format("/delete \"%s\"", name);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "load", value = "Load parcels into trucks.")
    public String load(
            @ShellOption(help = "User email", value = {"--user", "-u"}) String user,
            @ShellOption(help = "Path to a file containing parcels data (e.g., 'parcels.csv')", defaultValue = ShellOption.NULL) String file,
            @ShellOption(help = "List of trucks sizes, separated by '\\n' (e.g., '3x3\\n10x10')", defaultValue = ShellOption.NULL) String trucks,
            @ShellOption(help = "Type of loading (e.g., 'Одна машина - Одна посылка')", defaultValue = "Равномерное распределение") String type,
            @ShellOption(help = "Output format (e.g., 'text' or 'json')", defaultValue = "json-file") String out,
            @ShellOption(help = "Inline text description of parcels (e.g., 'Куб\\nПосылка тип 2')", defaultValue = ShellOption.NULL) String parcels,
            @ShellOption(help = "Output filename for saving results (e.g., 'trucks.json')", defaultValue = ShellOption.NULL) String output) {

        String command = loadCommandService.buildLoadCommand(user, file, trucks, type, out, parcels, output);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "unload", value = "Unload parcels from trucks.")
    public String unload(
            @ShellOption(help = "User email", value = {"--user", "-u"}) String user,
            @ShellOption(help = "Path to the input file (e.g., 'trucks.json')") String infile,
            @ShellOption(help = "Path to the output file (e.g., 'parcels-with-count.csv')") String outfile,
            @ShellOption(help = "Include count of parcels in the output.", defaultValue = "false") boolean count) {

        StringBuilder commandBuilder = new StringBuilder("/unload");

        commandBuilder.append(USER_SHORT_COMMAND).append(user).append("\"");

        commandBuilder.append(" -infile \"").append(infile).append("\"");
        commandBuilder.append(" -outfile \"").append(outfile).append("\"");

        if (count) {
            commandBuilder.append(" --withcount");
        }

        return commandHandler.handleCommand(commandBuilder.toString());
    }

    @ShellMethod(key = "billing", value = "Get billing details. Usage: billing -u <user> -from <start_date> -to <end_date>")
    public String billing(
            @ShellOption(help = "User email") String user,
            @ShellOption(help = "Start date (yyyy-MM-dd)") String from,
            @ShellOption(help = "End date (yyyy-MM-dd)") String to) {

        String commandBuilder = "/billing" + USER_SHORT_COMMAND + user + "\"" +
                " -from \"" + from + "\"" +
                " -to \"" + to + "\"";

        return commandHandler.handleCommand(commandBuilder);
    }
}