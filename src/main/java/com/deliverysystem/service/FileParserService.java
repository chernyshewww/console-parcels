package com.deliverysystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<char[][]> readParcelsFromFile(String fileName) {
        List<char[][]> parcels = new ArrayList<>();
        List<String> fileLines = FileReaderService.readFile(fileName);

        List<String> currentParcel = new ArrayList<>();
        for (String line : fileLines) {
            if (line.trim().isEmpty()) {
                addParcel(parcels, currentParcel);
            } else {
                currentParcel.add(line);
            }
        }
        addParcel(parcels, currentParcel);
        return parcels;
    }

    private static void addParcel(List<char[][]> parcels, List<String> lines) {
        if (!lines.isEmpty()) {
            String parcelString = String.join("\n", lines).trim();
            if (VALID_SEQUENCES.contains(parcelString)) {
                parcels.add(new ParcelService(lines).getData());
            } else {
                logger.error("Invalid parcel detected: {}", lines);
            }
            lines.clear();
        }
    }
}