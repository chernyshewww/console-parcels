package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.command.CommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@AllArgsConstructor
public class DeliverySystemShell {

    private final CommandHandler commandHandler;

    @ShellMethod(key = "create", value = "Create a new parcel. Usage: create <name> <symbol> <form>")
    public String create(
            @ShellOption(help = "Name of the parcel") String name,
            @ShellOption(help = "Symbol representing the parcel") char symbol,
            @ShellOption(help = "Form of the parcel (e.g., 'TT\\nTT')") String form) {
        String command = String.format("create %s %c %s", name, symbol, form);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "delete", value = "Delete a parcel by name. Usage: delete <name>")
    public String delete(
            @ShellOption(help = "Name of the parcel to delete") String name) {
        String command = String.format("delete %s", name);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "edit", value = "Edit a parcel. Usage: edit <id> <newName> <newForm> <newSymbol>")
    public String edit(
            @ShellOption(help = "ID of the parcel to edit") String id,
            @ShellOption(help = "New name for the parcel") String newName,
            @ShellOption(help = "New form for the parcel (e.g., 'UU\\nUU')") String newForm,
            @ShellOption(help = "New symbol for the parcel") char newSymbol) {
        String command = String.format("edit %s %s %s %c", id, newName, newForm, newSymbol);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "find", value = "Find a parcel by name. Usage: find <name>")
    public String find(
            @ShellOption(help = "Name of the parcel to find") String name) {
        String command = String.format("find %s", name);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "load", value = "Load parcels into trucks. Usage: load <parcels>")
    public String load(
            @ShellOption(help = "List of parcels to load (e.g., 'Parcel1 Parcel2')") String parcels) {
        String command = String.format("load %s", parcels);
        return commandHandler.handleCommand(command);
    }

    @ShellMethod(key = "unload", value = "Unload parcels from trucks. Usage: unload")
    public String unload() {
        String command = "unload";
        return commandHandler.handleCommand(command);
    }
}