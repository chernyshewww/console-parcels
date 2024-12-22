package com.deliverysystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextWriterService {

    public static void writeTrucksToTextFromJson(String inputJsonFile, String outputTextFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode trucksNode = objectMapper.readTree(new File(inputJsonFile));

            String result = processTrucks(trucksNode);

            writeToFile(outputTextFile, result);

            System.out.println("Successfully wrote trucks to text file.");
        } catch (IOException e) {
            System.err.println("Error while processing trucks: " + e.getMessage());
        }
    }

    private static String processTrucks(JsonNode trucksNode) {
        StringBuilder result = new StringBuilder();

        trucksNode.iterator().forEachRemaining(truckNode -> {
            truckNode.fields().forEachRemaining(entry -> {
                List<String> grid = extractGridData(entry.getValue());
                Map<Integer, Integer> charFrequency = calculateCharFrequency(grid);
                result.append(generateFrequencyOutput(charFrequency));
            });
        });

        return result.toString();
    }

    private static List<String> extractGridData(JsonNode gridNode) {
        List<String> grid = new ArrayList<>();
        gridNode.forEach(node -> grid.add(node.asText()));
        return grid;
    }

    private static Map<Integer, Integer> calculateCharFrequency(List<String> grid) {
        Map<Integer, Integer> numberFrequency = new HashMap<>();

        for (String row : grid) {
            String sanitizedRow = row.substring(1, row.length() - 1).trim();
            for (char ch : sanitizedRow.toCharArray()) {
                if (ch != '+') {
                    int number = Character.getNumericValue(ch);
                    numberFrequency.put(number, numberFrequency.getOrDefault(number, 0) + 1);
                }
            }
        }

        return numberFrequency;
    }

    private static String generateFrequencyOutput(Map<Integer, Integer> numberFrequency) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<Integer, Integer> entry : numberFrequency.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();

            if (value % key == 0) {
                for (int j = 1; j <= key; j++) {
                    if (shouldAddNewLine(j, value)) {
                        result.append("\n");
                    }
                    result.append(key);
                }

                result.append("\n\n");
            }
        }

        return result.toString();
    }

    private static boolean shouldAddNewLine(int index, int value) {
        return (index == 4 && value % 5 != 0 && value % 4 != 0) ||
                (index == 7 && value % 7 != 0 && value % 8 != 0) ||
                (index == 5 && value % 8 == 0);
    }

    private static void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
