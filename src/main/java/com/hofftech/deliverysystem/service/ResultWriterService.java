package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.exception.FileProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ResultWriterService {

    private final ObjectMapper objectMapper;

    public ResultWriterService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void writeTrucksToJson(List<Truck> trucks, String fileName) {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            writeCustomFormattedTrucks(fileName, trucks);
            log.info("Trucks written to JSON file with custom formatting: {}", fileName);
        } catch (FileProcessingException e) {
            log.error("Error while writing trucks to JSON: {}", e.getMessage(), e);
            throw new RuntimeException(e);  // Re-throwing the custom exception
        }
    }

    private void writeCustomFormattedTrucks(String fileName, List<Truck> trucks) throws FileProcessingException {
        StringBuilder customJson = new StringBuilder();
        customJson.append("[\n");

        for (int i = 0; i < trucks.size(); i++) {
            Truck truck = trucks.get(i);
            customJson.append("  {\n");

            customJson.append("    \"Truck #").append(i + 1).append("\": [\n");

            for (int j = 0; j < truck.getGrid().length; j++) {
                char[] row = truck.getGrid()[j];

                String rowString = "+" + new String(row).replace("\u0000", " ") + "+";

                customJson.append("      \"").append(rowString).append("\"");

                if (j < truck.getGrid().length - 1) {
                    customJson.append(",");
                }
                customJson.append("\n");
            }

            customJson.append("      \"++++++++\"\n");

            customJson.append("    ]\n");
            customJson.append("  }");

            if (i < trucks.size() - 1) {
                customJson.append(",");
            }
            customJson.append("\n");
        }

        customJson.append("]");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(customJson.toString());
        } catch (IOException e) {
            log.error("Failed to write to file: {}", fileName, e);
            throw new FileProcessingException("Error writing to file: " + fileName, e);
        }
    }
}
