package com.hofftech.deliverysystem.shell.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;


public interface DeliverySystemRestClient {

    @PostExchange("/api/commands/create")
    String createParcel(
            @RequestParam String name,
            @RequestParam String form,
            @RequestParam char symbol);

    @GetExchange("/api/commands/find")
    String findParcel(
            @RequestParam String name);

    @DeleteExchange("/api/commands/delete")
    String deleteParcel(
            @RequestParam  String name);

    @PostExchange("/api/commands/edit")
    String edit(
                @RequestParam String id,
                @RequestParam String name,
                @RequestParam String form,
                @RequestParam char symbol);

    @PostExchange("/api/commands/load")
    String load(
            @RequestParam String user,
            @RequestParam(required = false) String trucks,
            @RequestParam(defaultValue = "Равномерное распределение") String type,
            @RequestParam(defaultValue = "json-file") String out,
            @RequestParam(required = false) String parcels,
            @RequestParam(required = false) String output);

    @GetExchange("/api/commands/billing")
    String billing (@RequestParam String user,
                          @RequestParam String from,
                          @RequestParam String to);

    @PostExchange("/api/commands/unload")
    String unload(@RequestParam String user,
                 @RequestParam String outfile,
                 @RequestParam(defaultValue = "false") boolean count);

}