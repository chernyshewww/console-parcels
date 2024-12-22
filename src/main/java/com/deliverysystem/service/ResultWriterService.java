package com.deliverysystem.service;

import com.deliverysystem.model.Truck;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ResultWriterService {

    public static void writeTrucksToJson(List<Truck> trucks, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            writeCustomFormattedTrucks(fileName, trucks);
            System.out.println("Trucks written to JSON file with custom formatting: " + fileName);
        } catch (IOException e) {
            System.err.println("Error while writing trucks to JSON: " + e.getMessage());
            throw new IOException(e);
        }
    }

    private static void writeCustomFormattedTrucks(String fileName, List<Truck> trucks) throws IOException {
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

                if (j < truck.getGrid().length) {
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

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(customJson.toString());
        writer.close();
    }
}