package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.command.CommandDispatcher;
import com.hofftech.deliverysystem.service.LoadCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commands")
@RequiredArgsConstructor
@Tag(name = "Delivery System Commands", description = "APIs for managing delivery system commands (e.g., create, find, edit, delete, load, unload, billing).")
public class DeliverySystemRestController {

    private final CommandDispatcher commandDispatcher;
    private final LoadCommandService loadCommandService;

    @PostMapping("/create")
    @Operation(
            summary = "Create a new command",
            description = "This endpoint creates a new command with the provided parameters (name, form, symbol).",
            tags = {"Command Management"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Command successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    public String create(@RequestParam @Parameter(description = "The name of the command") String name,
                         @RequestParam @Parameter(description = "The form of the command") String form,
                         @RequestParam @Parameter(description = "The symbol associated with the command") char symbol) {
        String command = String.format("/create -name \"%s\" -form \"%s\" -symbol \"%c\"", name, form, symbol);
        return commandDispatcher.dispatchCommand(command);
    }

    @GetMapping("/find")
    @Operation(
            summary = "Find a command by name",
            description = "This endpoint retrieves a command based on its name.",
            tags = {"Command Management"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Command found"),
            @ApiResponse(responseCode = "404", description = "Command not found")
    })
    public String find(@RequestParam @Parameter(description = "The name of the command") String name) {
        String command = String.format("/find \"%s\"", name);
        return commandDispatcher.dispatchCommand(command);
    }

    @PostMapping("/edit")
    @Operation(
            summary = "Edit an existing command",
            description = "This endpoint allows you to edit an existing command by specifying the new parameters.",
            tags = {"Command Management"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Command successfully edited"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "404", description = "Command not found")
    })
    public String edit(@RequestParam @Parameter(description = "The name of the command") String name,
                       @RequestParam @Parameter(description = "The form of the command") String form,
                       @RequestParam @Parameter(description = "The symbol associated with the command") char symbol) {
        String command = String.format("/edit -id \"%s\" -name \"%s\" -form \"%s\" -symbol \"%c\"", name, name, form, symbol);
        return commandDispatcher.dispatchCommand(command);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete a command",
            description = "This endpoint deletes a command by its name.",
            tags = {"Command Management"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Command successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Command not found")
    })
    public String delete(@RequestParam @Parameter(description = "The name of the command to delete") String name) {
        String command = String.format("/delete \"%s\"", name);
        return commandDispatcher.dispatchCommand(command);
    }

    @PostMapping("/load")
    @Operation(
            summary = "Load commands",
            description = "This endpoint loads commands for the user with additional parameters like trucks, type, output format, etc.",
            tags = {"Load Commands"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commands successfully loaded"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    public String load(@RequestParam @Parameter(description = "The user for whom to load commands") String user,
                       @RequestParam(required = false) @Parameter(description = "The number of trucks to use") String trucks,
                       @RequestParam(defaultValue = "Равномерное распределение") @Parameter(description = "The type of distribution") String type,
                       @RequestParam(defaultValue = "json-file") @Parameter(description = "The output format (json-file by default)") String out,
                       @RequestParam(required = false) @Parameter(description = "The parcels to load") String parcels,
                       @RequestParam(required = false) @Parameter(description = "The output file") String output) {

        String command = loadCommandService.buildLoadCommand(user, trucks, type, out, parcels, output);
        return commandDispatcher.dispatchCommand(command);
    }

    @PostMapping("/unload")
    @Operation(
            summary = "Unload commands",
            description = "This endpoint unloads commands for the user, optionally with a count flag.",
            tags = {"Unload Commands"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commands successfully unloaded"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    public String unload(@RequestParam @Parameter(description = "The user for whom to unload commands") String user,
                         @RequestParam @Parameter(description = "The output file for the unloaded commands") String outfile,
                         @RequestParam(defaultValue = "false") @Parameter(description = "Flag to include count") boolean count) {
        StringBuilder commandBuilder = new StringBuilder("/unload");

        commandBuilder.append(" -u \"").append(user).append("\"");
        commandBuilder.append(" -outfile \"").append(outfile).append("\"");

        if (count) {
            commandBuilder.append(" --withcount");
        }

        return commandDispatcher.dispatchCommand(commandBuilder.toString());
    }

    @GetMapping("/billing")
    @Operation(
            summary = "Get billing information",
            description = "This endpoint retrieves billing information for a user within the specified period.",
            tags = {"Billing"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Billing information retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid date format or input parameters")
    })
    public String billing(@RequestParam @Parameter(description = "The user for whom to retrieve billing information") String user,
                          @RequestParam @Parameter(description = "Start date for the billing period") String from,
                          @RequestParam @Parameter(description = "End date for the billing period") String to) {
        String commandBuilder = "/billing" +
                " -u \"" + user + "\"" +
                " -from \"" + from + "\"" +
                " -to \"" + to + "\"";

        return commandDispatcher.dispatchCommand(commandBuilder);
    }
}
