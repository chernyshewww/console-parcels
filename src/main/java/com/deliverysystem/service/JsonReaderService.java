package com.deliverysystem.service;

import com.deliverysystem.model.Truck;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReaderService {

    public static List<Truck> readTrucksFromJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(fileName));
        List<Truck> trucks = new ArrayList<>();

        for (JsonNode truckNode : rootNode) {
            Truck truck = new Truck();

            JsonNode gridNode = truckNode.fields().next().getValue();
            for (int i = 0; i < gridNode.size() - 1; i++) {
                String row = gridNode.get(i).asText();
                // Remove the '+' at the ends
                truck.getGrid()[i] = row.substring(1, row.length() - 1).toCharArray();
            }

            trucks.add(truck);
        }

        return trucks;
    }
}