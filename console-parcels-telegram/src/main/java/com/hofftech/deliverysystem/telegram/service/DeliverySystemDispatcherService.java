package com.hofftech.deliverysystem.telegram.service;

import com.hofftech.deliverysystem.telegram.exception.InvalidCommandException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
@Slf4j
public class DeliverySystemDispatcherService {

    private final DeliverySystemService deliverySystemService;

    private final Map<String, Function<String, String>> commandHandlers = new HashMap<>();

    @PostConstruct
    public void init() {
        commandHandlers.put("/load", deliverySystemService::sendLoadCommand);
        commandHandlers.put("/create", deliverySystemService::sendCreateCommand);
        commandHandlers.put("/find", deliverySystemService::sendFindCommand);
        commandHandlers.put("/edit", deliverySystemService::sendEditCommand);
        commandHandlers.put("/delete", deliverySystemService::sendDeleteCommand);
        commandHandlers.put("/unload", deliverySystemService::sendUnloadCommand);
        commandHandlers.put("/billing", deliverySystemService::sendBillingCommand);
    }

    /**
     * Dispatches the command based on the first word in the input string.
     *
     * @param input The input command string.
     * @return The result of the dispatched command.
     */
    public String dispatchCommand(String input) {
        if (input == null || input.isBlank()) {
            log.error("Command input is empty or null");
            return "Ошибка: Введите правильную команду.";
        }

        String commandKey = input.split("\\s+")[0];

        Function<String, String> handler = commandHandlers.get(commandKey);

        if (handler == null) {
            log.error("No handler found for command: {}", commandKey);
            return "Ошибка: Неизвестная команда.";
        }

        try {
            return handler.apply(input);
        } catch (InvalidCommandException e) {
            log.error("Invalid command format: {}", input, e);
            return e.getMessage();
        } catch (Exception e) {
            log.error("An unexpected error occurred while executing the command: {}", input, e);
            return "Ошибка: Произошла непредвиденная ошибка при выполнении команды.";
        }
    }
}
