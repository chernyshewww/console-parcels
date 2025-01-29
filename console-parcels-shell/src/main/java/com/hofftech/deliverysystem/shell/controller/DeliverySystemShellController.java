package com.hofftech.deliverysystem.shell.controller;

import com.hofftech.deliverysystem.shell.client.DeliverySystemRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class DeliverySystemShellController {

    private final DeliverySystemRestClient deliverySystemRestClient;

    @ShellMethod(key = "create", value = "Create a new parcel. Usage: create <name> <symbol> <form>")
    public String create(
            @ShellOption(help = "Name of the parcel") String name,
            @ShellOption(help = "Form of the parcel (e.g., 'TT\\nTT')") String form,
            @ShellOption(help = "Symbol representing the parcel") char symbol) {

        return deliverySystemRestClient.createParcel(name, form, symbol);
    }

    @ShellMethod(key = "find", value = "Find a parcel by name. Usage: find <name>")
    public String find(@ShellOption(help = "Name of the parcel to find") String name) {
        return deliverySystemRestClient.findParcel(name);
    }

    @ShellMethod(key = "delete", value = "Delete a parcel by name. Usage: delete <name>")
    public String delete(@ShellOption(help = "Name of the parcel to delete") String name) {
        return deliverySystemRestClient.deleteParcel(name);
    }

    @ShellMethod(key = "edit", value = "Edit a parcel. Usage: edit <id> <newName> <newForm> <newSymbol>")
    public String edit(
            @ShellOption(help = "ID of the parcel to edit") String id,
            @ShellOption(help = "New name for the parcel") String name,
            @ShellOption(help = "New form for the parcel (e.g., 'UU\\nUU')") String form,
            @ShellOption(help = "New symbol for the parcel") char symbol) {
        return deliverySystemRestClient.edit(id, name, form, symbol);
    }

    @ShellMethod(key = "load", value = "Load parcels into trucks.")
    public String load(
            @ShellOption(help = "User email") String user,
            @ShellOption(help = "Trucks sizes") String trucks,
            @ShellOption(help = "Loading type") String type,
            @ShellOption(help = "Output format") String out,
            @ShellOption(help = "Parcels data") String parcels,
            @ShellOption(help = "Output file") String output) {

        return deliverySystemRestClient.load(user, trucks, type, out, parcels, output);
    }

    @ShellMethod(key = "unload", value = "Unload parcels from trucks.")
    public String unload(
            @ShellOption(help = "User email", value = {"--user", "-u"}) String user,
            @ShellOption(help = "Path to the output file (e.g., 'parcels-with-count.csv')") String outfile,
            @ShellOption(help = "Include count of parcels in the output.", defaultValue = "false") boolean count) {

        return deliverySystemRestClient.unload(user, outfile, count);
    }

    @ShellMethod(key = "billing", value = "Get billing details. Usage: billing -u <user> -from <start_date> -to <end_date>")
    public String billing(
            @ShellOption(help = "User email") String user,
            @ShellOption(help = "Start date (yyyy-MM-dd)") String from,
            @ShellOption(help = "End date (yyyy-MM-dd)") String to) {

        return deliverySystemRestClient.billing(user, from, to);
    }
}
