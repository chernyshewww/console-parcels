package com.deliverysystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Class for parsing file and validate incoming parcels
//todo убрать комменты, если хочешь можешь написать документацию. Но если ведешь документаци - види для всех классов
public class FileParserService {
    private static final Logger logger = LoggerFactory.getLogger(FileParserService.class);

    // PROGRAM WILL SKIP ALL THE INVALID SEQUENCES AND WRITE WARNING ABOUT IT
    private static final List<String> VALID_SEQUENCES = Arrays.asList(
            "1",
            "22",
            "333",
            "4444",
            "55555",
            "666\n666",
            "777\n7777",
            "8888\n8888",
            "999\n999\n999"
    );
    //todo вынеси чтение файла в отдельный сервис.
    public static List<char[][]> readParcelsFromFile(String fileName) {
        List<char[][]> parcels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<String> currentParcel = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    addParcel(parcels, currentParcel);
                } else {
                    currentParcel.add(line);
                }
            }
            addParcel(parcels, currentParcel);

        } catch (IOException e) {
            logger.error("Error reading file: {}", e.getMessage());
        }
        return parcels;
    }

    private static void addParcel(List<char[][]> parcels, List<String> lines) {
        if (!lines.isEmpty()) {
            String parcelString = String.join("\n", lines).trim();
            if (VALID_SEQUENCES.contains(parcelString)) {
                parcels.add(new ParcelService(lines).getData());
            } else {
                //todo мне кажется это скорее error. Не валидная посылка в файле = кривой файл
                logger.warn("Invalid parcel skipped: {}", lines);
            }
            lines.clear();
        }
    }
}