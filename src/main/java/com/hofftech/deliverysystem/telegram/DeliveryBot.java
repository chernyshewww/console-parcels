package com.hofftech.deliverysystem.telegram;

import com.hofftech.deliverysystem.command.CreateCommand;
import com.hofftech.deliverysystem.command.DeleteCommand;
import com.hofftech.deliverysystem.command.EditCommand;
import com.hofftech.deliverysystem.command.FindCommand;
import com.hofftech.deliverysystem.command.LoadCommand;
import com.hofftech.deliverysystem.command.UnloadCommand;
import com.hofftech.deliverysystem.exception.InvalidCommandException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.CommandParserService;
import com.hofftech.deliverysystem.service.FileService;
import com.hofftech.deliverysystem.service.OutputService;
import com.hofftech.deliverysystem.service.ParcelService;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.strategy.LoadingStrategy;
import com.hofftech.deliverysystem.strategy.StrategyHelper;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.hofftech.deliverysystem.constants.Constant.HELP_TEXT;

@Slf4j
@RequiredArgsConstructor
public class DeliveryBot extends TelegramLongPollingBot {

    private final ParcelService parcelService;
    private final TruckService truckService;
    private final StrategyHelper strategyHelper;
    private final CommandParserService commandParserService;
    private final FileService fileService;
    private final OutputService outputService;
    private final FormHelper formHelper;
    private final String botToken;
    private final String botUsername;

    /**
     * Returns the username of the bot. This method is required to authenticate the bot.
     *
     * @return The bot's username.
     */
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * Returns the token of the bot. This method is required to authenticate the bot.
     *
     * @return The bot's token.
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Processes incoming updates (messages) from users. Based on the message text, the bot determines
     * which command is being issued and responds accordingly.
     *
     * @param update The incoming update containing the message sent by the user.
     */
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        long chatId = message.getChatId();

        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);

        if (text.equals("/help")) {
            sendHelpMessage(chatId);
        } else if (text.startsWith("/create")) {
            handleCreateCommand(text, responseMessage);
        } else if (text.startsWith("/find")) {
            handleFindCommand(text, responseMessage);
        } else if (text.startsWith("/edit")) {
            handleEditCommand(text, responseMessage);
        } else if (text.startsWith("/load")) {
            handleLoadCommand(text, responseMessage);
        } else if (text.startsWith("/unload")) {
            handleUnloadCommand(text, responseMessage);
        } else if (text.startsWith("/delete")) {
            handleDeleteCommand(text, responseMessage);
        } else {
            responseMessage.setText("Команда не распознана. Пожалуйста, используйте правильный формат.");
        }

        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCreateCommand(String text, SendMessage responseMessage) {
        try {
            CreateCommand commandData = commandParserService.parseCreateCommand(text);
            char[][] form = formHelper.parseForm(commandData.getForm(), commandData.getSymbol());

            parcelService.createParcel(commandData.getName(), commandData.getSymbol(), form);

            String formattedResponse = outputService.formatCreateResponse(commandData.getName(), form);
            responseMessage.setText(formattedResponse);
        } catch (InvalidCommandException e) {
            responseMessage.setText(e.getMessage());
        } catch (IllegalArgumentException e) {
            responseMessage.setText("Ошибка при создании посылки: " + e.getMessage());
        }
    }

    private void handleFindCommand(String text, SendMessage responseMessage) {
        try {
            FindCommand commandData = commandParserService.parseFindCommand(text);

            String response = parcelService.findParcelInFile(commandData.getParcelName());
            responseMessage.setText(response);
        } catch (InvalidCommandException e) {
            responseMessage.setText(e.getMessage());
        } catch (Exception e) {
            responseMessage.setText("Ошибка при поиске посылки: " + e.getMessage());
            log.error("Error while processing /find command", e);
        }
    }

    private void handleEditCommand(String text, SendMessage responseMessage) {
        try {
            EditCommand commandData = commandParserService.parseEditCommand(text);

            String result = parcelService.editParcelInFile(
                    commandData.getId(),
                    commandData.getNewName(),
                    commandData.getNewForm(),
                    commandData.getNewSymbol()
            );

            responseMessage.setText(result);
        } catch (InvalidCommandException e) {
            responseMessage.setText(e.getMessage());
        } catch (Exception e) {
            responseMessage.setText("Ошибка при редактировании посылки: " + e.getMessage());
            log.error("Error handling /edit command", e);
        }
    }

    private void handleDeleteCommand(String text, SendMessage responseMessage) {
        try {
            DeleteCommand commandData = commandParserService.parseDeleteCommand(text);

            String result = parcelService.deleteParcelInFile(commandData.getParcelName());

            responseMessage.setText(result);
        } catch (InvalidCommandException e) {
            responseMessage.setText(e.getMessage());
        } catch (Exception e) {
            responseMessage.setText("Ошибка при удалении посылки: " + e.getMessage());
            log.error("Error handling /delete command", e);
        }
    }

    private void handleLoadCommand(String text, SendMessage responseMessage) {
        try {
            LoadCommand commandData = commandParserService.parseLoadCommand(text);
            List<Parcel> parcels = parcelService.loadParcels(commandData);
            List<Truck> trucks = truckService.parseTruckSizes(commandData.getTrucksText());
            LoadingStrategy strategy = strategyHelper.determineStrategy(commandData.getStrategyType());

            if (strategy == null) {
                responseMessage.setText("Ошибка! Указан неизвестный тип стратегии.");
                return;
            }

            List<Truck> loadedTrucks = strategy.loadParcels(parcels, trucks);
            log.info("Successfully loaded parcels.");

            switch (commandData.getOutputType()) {
                case "text":
                    responseMessage.setText(outputService.generateLoadOutput(loadedTrucks));
                    break;
                case "json-file":
                    outputService.saveJsonOutput(commandData.getOutputFileName(), loadedTrucks, responseMessage);
                    break;
                default:
                    break;
            }
        } catch (InvalidCommandException e) {
            responseMessage.setText(e.getMessage());
        } catch (IllegalStateException e) {
            log.error("Failed to load parcels: {}", e.getMessage());
            responseMessage.setText("Ошибка! Не удалось разместить все посылки в грузовиках. Проверьте размеры и количество.");
        } catch (Exception e) {
            responseMessage.setText("Произошла ошибка: " + e.getMessage());
            log.error("Error while processing /load command", e);
        }
    }

    private void handleUnloadCommand(String text, SendMessage responseMessage) {
        try {
            UnloadCommand commandData = commandParserService.parseUnloadCommand(text);

            List<Truck> trucks = truckService.loadTrucksFromFile(commandData.getInputFileName());

            List<Parcel> parcels = parcelService.unloadParcelsFromTrucks(trucks);

            String result = commandData.isWithCount()
                    ? outputService.generateParcelCountOutput(parcels)
                    : outputService.generateParcelOutput(parcels);

            fileService.saveToFile(commandData.getOutputFileName(), result);

            responseMessage.setText("Выгрузка завершена. Результат сохранён в файл: " + commandData.getOutputFileName());
        } catch (InvalidCommandException e) {
            responseMessage.setText(e.getMessage());
        } catch (Exception e) {
            responseMessage.setText("Произошла ошибка при выгрузке посылок: " + e.getMessage());
            log.error("Error while processing /unload command", e);
        }
    }

    private void sendHelpMessage(long chatId) {
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText(HELP_TEXT);

        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
