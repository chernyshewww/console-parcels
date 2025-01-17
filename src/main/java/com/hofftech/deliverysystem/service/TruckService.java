package com.hofftech.deliverysystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofftech.deliverysystem.exception.TruckFileReadException;
import com.hofftech.deliverysystem.model.Truck;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Service class for handling truck-related operations such as generating truck views,
 * parsing truck sizes from a text, and loading trucks from a file.
 */
@Slf4j
public class TruckService {

    /**
     * Generates a string representation of a truck's grid, where each cell in the grid is
     * represented by a space if it is empty or the corresponding character for filled cells.
     *
     * @param truck The truck whose grid view is to be generated.
     * @return A string representing the truck's grid.
     */
    public String generateTruckView(Truck truck) {
        StringBuilder view = new StringBuilder();
        char[][] grid = truck.getGrid();

        for (char[] row : grid) {
            for (char cell : row) {
                view.append(cell == '\0' ? ' ' : cell);
            }
            view.append("\n");
        }
        return view.toString();
    }

    /**
     * Parses a string containing truck dimensions (in the format "width x height")
     * and converts them into a list of Truck objects.
     *
     * Each truck's width and height are separated by 'x', and each truck's dimensions are
     * separated by a new line.
     *
     * @param trucksText The string containing truck dimensions.
     * @return A list of Truck objects created based on the parsed dimensions.
     */
    public List<Truck> parseTruckSizes(String trucksText) {
        List<Truck> trucks = new ArrayList<>();
        String[] truckDimensions = trucksText.split("\\\\n");

        for (String dimension : truckDimensions) {
            String[] dimensions = dimension.split("x");
            if (dimensions.length == 2) {
                try {
                    int width = Integer.parseInt(dimensions[0].trim());
                    int height = Integer.parseInt(dimensions[1].trim());
                    trucks.add(new Truck(width, height));
                } catch (NumberFormatException e) {
                    log.error("Ошибка при разборе размера грузовика: {}", dimension);
                }
            }
        }

        return trucks;
    }

    /**
     * Loads a list of trucks from a file in JSON format. The trucks are deserialized
     * into Truck objects using Jackson's ObjectMapper.
     *
     * @param inputFileName The name of the input file containing truck data in JSON format.
     * @return A list of Truck objects loaded from the file.
     * @throws TruckFileReadException If an error occurs while reading the truck file.
     */
    public List<Truck> loadTrucksFromFile(String inputFileName) throws TruckFileReadException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(inputFileName), new TypeReference<List<Truck>>() {});
        } catch (IOException e) {
            throw new TruckFileReadException("Ошибка при чтении файла грузовиков: " + inputFileName, e);
        }
    }
}
