package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.exception.FileReaderException;
import com.hofftech.deliverysystem.model.Truck;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class JsonReaderService {

    public List<Truck> readTrucksFromJson(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(fileName));
            List<Truck> trucks = new ArrayList<>();

            for (JsonNode truckNode : rootNode) {
                Truck truck = new Truck();

                JsonNode gridNode = truckNode.fields().next().getValue();
                for (int i = 0; i < gridNode.size() - 1; i++) {
                    String row = gridNode.get(i).asText();
                    truck.getGrid()[i] = row.substring(1, row.length() - 1).toCharArray();
                }

                trucks.add(truck);
            }
            return trucks;
        } catch (IOException e) {
            log.error("Error reading file '{}': {}", fileName, e.getMessage());
            throw new FileReaderException("Failed to read file: " + fileName, e);
        }
    }
}