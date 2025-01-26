package com.hofftech.deliverysystem.controller;

import com.hofftech.deliverysystem.command.CommandDispatcher;
import com.hofftech.deliverysystem.service.LoadCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commands")
@RequiredArgsConstructor
public class DeliverySystemRestController {

    private final CommandDispatcher commandDispatcher;
    private final LoadCommandService loadCommandService;

    @PostMapping("/create")
    public String create(@RequestParam String name,
                         @RequestParam String form,
                         @RequestParam char symbol) {
        String command = String.format("/create -name \"%s\" -form \"%s\" -symbol \"%c\"", name, form, symbol);
        return commandDispatcher.dispatchCommand(command);
    }

    @GetMapping("/find")
    public String find(@RequestParam String name) {
        String command = String.format("/find \"%s\"", name);
        return commandDispatcher.dispatchCommand(command);
    }

    @PostMapping("/edit")
    public String edit(@RequestParam String name,
                       @RequestParam String form,
                       @RequestParam char symbol) {
        String command = String.format("/edit -id \"%s\" -name \"%s\" -form \"%s\" -symbol \"%c\"", name, name, form, symbol);
        return commandDispatcher.dispatchCommand(command);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String name) {
        String command = String.format("/delete \"%s\"", name);
        return commandDispatcher.dispatchCommand(command);
    }

    @PostMapping("/load")
    public String load(@RequestParam String user,
                       @RequestParam(required = false) String trucks,
                       @RequestParam(defaultValue = "Равномерное распределение") String type,
                       @RequestParam(defaultValue = "json-file") String out,
                       @RequestParam(required = false) String parcels,
                       @RequestParam(required = false) String output) {

        String command = loadCommandService.buildLoadCommand(user, trucks, type, out, parcels, output);
        return commandDispatcher.dispatchCommand(command);
    }
    @PostMapping("/unload")
    public String unload(@RequestParam String user,
                         @RequestParam String infile,
                         @RequestParam String outfile,
                         @RequestParam(defaultValue = "false") boolean count) {
        StringBuilder commandBuilder = new StringBuilder("/unload");

        commandBuilder.append(" -u \"").append(user).append("\"");
        commandBuilder.append(" -infile \"").append(infile).append("\"");
        commandBuilder.append(" -outfile \"").append(outfile).append("\"");

        if (count) {
            commandBuilder.append(" --withcount");
        }

        return commandDispatcher.dispatchCommand(commandBuilder.toString());
    }

    @GetMapping("/billing")
    public String billing(@RequestParam String user,
                          @RequestParam String from,
                          @RequestParam String to) {
        String commandBuilder = "/billing" +
                " -u \"" + user + "\"" +
                " -from \"" + from + "\"" +
                " -to \"" + to + "\"";

        return commandDispatcher.dispatchCommand(commandBuilder);
    }
}
