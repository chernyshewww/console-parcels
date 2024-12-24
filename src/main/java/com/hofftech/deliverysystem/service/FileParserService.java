package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FileParserService {

    private final FileReaderService fileReaderService;

    private static final Set<String> VALID_SEQUENCES = Set.of(
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

    public FileParserService(FileReaderService fileReaderService) {
        this.fileReaderService = fileReaderService;
    }

    public List<Parcel> readParcelsFromFile(String fileName) {
        List<Parcel> parcels = new ArrayList<>();
        List<String> fileLines = fileReaderService.readFile(fileName);

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

    private void addParcel(List<Parcel> parcels, List<String> lines) {
        if (!lines.isEmpty()) {
            String parcelString = String.join("\n", lines).trim();
            if (VALID_SEQUENCES.contains(parcelString)) {
                char[][] parcelData = convertLinesToCharArray(lines);
                parcels.add(new Parcel(parcelData));
            } else {
                log.error("Invalid parcel detected: {}", lines);
            }
            lines.clear();
        }
    }

    private char[][] convertLinesToCharArray(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }
}
