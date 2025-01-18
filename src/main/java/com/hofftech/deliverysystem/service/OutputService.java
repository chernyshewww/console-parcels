package com.hofftech.deliverysystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofftech.deliverysystem.exception.JsonGenerationException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.util.OutputHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for generating various output formats, such as text and JSON,
 * for parcels and trucks, and saving the output to files.
 */
@RequiredArgsConstructor
@Service
public class OutputService {

    private static final String PARCEL = "Посылка: ";
    private final TruckService truckService;
    private final OutputHelper outputHelper;
    private final FileService fileService;

    /**
     * Generates a textual output for a list of parcels, displaying their names.
     *
     * @param parcels The list of parcels to be included in the output.
     * @return A formatted string with the names of all parcels.
     */
    public String generateParcelOutput(List<Parcel> parcels) {
        StringBuilder output = new StringBuilder();
        for (Parcel parcel : parcels) {
            output.append(PARCEL).append(parcel.getName()).append("\n");
        }
        return output.toString();
    }

    /**
     * Generates a textual output for a list of trucks, displaying the truck layout,
     * their parcels, and their coordinates.
     *
     * @param trucks The list of trucks to be included in the output.
     * @return A formatted string representing the trucks' layout and parcels.
     */
    public String generateLoadOutput(List<Truck> trucks) {
        StringBuilder output = new StringBuilder();

        for (Truck truck : trucks) {

            output.append("Кузов грузовика:").append("\n");

            output.append("+").append(" ".repeat(truck.getWidth())).append("+").append("\n");

            for (int i = 0; i < truck.getHeight(); i++) {
                output.append("+");
                output.append(truckService.generateTruckView(truck).split("\n")[i]);
                output.append("+").append("\n");
            }

            output.append("+".repeat(truck.getWidth() + 2)).append("\n");

            for (Parcel parcel : truck.getParcels()) {
                output.append(PARCEL).append(parcel.getName()).append("\n");
                output.append("Координаты размещения: ").append(parcel.getPlacedCoordinates()).append("\n");
            }

            output.append("---------------").append("\n");
        }

        return output.toString();
    }

    /**
     * Generates a textual output that lists the parcels along with their counts.
     *
     * @param parcels The list of parcels to count and include in the output.
     * @return A formatted string showing each parcel and its count.
     */
    public String generateParcelCountOutput(List<Parcel> parcels) {
        Map<String, Integer> parcelCount = new HashMap<>();
        for (Parcel parcel : parcels) {
            parcelCount.put(parcel.getName(), parcelCount.getOrDefault(parcel.getName(), 0) + 1);
        }

        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, Integer> entry : parcelCount.entrySet()) {
            output.append(PARCEL).append(entry.getKey())
                    .append(", Количество: ").append(entry.getValue())
                    .append("\n");
        }
        return output.toString();
    }

    /**
     * Formats a response for creating a parcel, including its name and form.
     *
     * @param name The name of the parcel.
     * @param form The form (layout) of the parcel.
     * @return A formatted string including the parcel's name and form.
     */
    public String formatCreateResponse(String name, char[][] form) {
        StringBuilder formBuilder = new StringBuilder();
        for (char[] row : form) {
            formBuilder.append(new String(row)).append("\n");
        }

        if (formBuilder.length() > 0) {
            formBuilder.setLength(formBuilder.length() - 1);
        }

        return String.format("id(name): \"%s\"\nform:\n%s", name, formBuilder.toString());
    }

    /**
     * Saves the generated JSON output to a file and sends a response message.
     *
     * @param outputFileName The name of the output file where the JSON will be saved.
     * @param loadedTrucks The list of trucks to be included in the JSON output.
     * @param responseMessage The message to be sent to the user with the result.
     */
    public void saveJsonOutput(String outputFileName, List<Truck> loadedTrucks, SendMessage responseMessage){
        if (outputFileName == null || outputFileName.isBlank()) {
            responseMessage.setText("Ошибка! Для вывода в файл JSON укажите имя файла через -out-filename.");
            return;
        }

        String jsonOutput = generateJsonOutput(loadedTrucks);
        fileService.saveToFile(outputFileName, jsonOutput);
        responseMessage.setText("Успешно! Результат сохранен в файл: " + outputFileName);
    }

    /**
     * Generates a JSON string representing a list of trucks and their data.
     *
     * @param trucks The list of trucks to be included in the JSON output.
     * @return A formatted JSON string representing the trucks' data.
     * @throws JsonGenerationException If an error occurs during JSON generation.
     */
    private String generateJsonOutput(List<Truck> trucks) {
        List<Map<String, Object>> truckDataList = new ArrayList<>();

        for (Truck truck : trucks) {
            Map<String, Object> truckData = outputHelper.createTruckData(truck);
            truckDataList.add(truckData);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(truckDataList);
        } catch (JsonProcessingException e) {
            throw new JsonGenerationException("Ошибка при генерации JSON", e);
        }
    }
}
