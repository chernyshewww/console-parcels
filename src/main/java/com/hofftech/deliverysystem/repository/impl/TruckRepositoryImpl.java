package com.hofftech.deliverysystem.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofftech.deliverysystem.exception.TruckFileReadException;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.repository.TruckRepositoryInterface;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class TruckRepositoryImpl implements TruckRepositoryInterface {

    /**
     * Loads a list of trucks from a file in JSON format. The trucks are deserialized
     * into Truck objects using Jackson's ObjectMapper.
     *
     * @param inputFileName The name of the input file containing truck data in JSON format.
     * @return A list of Truck objects loaded from the file.
     * @throws TruckFileReadException If an error occurs while reading the truck file.
     */
    @Override
    public List<Truck> loadFromFile(String inputFileName) throws TruckFileReadException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(inputFileName), new TypeReference<List<Truck>>() {});
        } catch (IOException e) {
            throw new TruckFileReadException("Ошибка при чтении файла грузовиков: " + inputFileName, e);
        }
    }
}
