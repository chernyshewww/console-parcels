package com.deliverysystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ParcelService {
    private static final Logger logger = LoggerFactory.getLogger(ParcelService.class);
    private final char[][] data;

    public ParcelService(List<String> lines) {
        this.data = convertToMatrix(lines);
    }

    public char[][] getData() {
        return data;
    }

    private char[][] convertToMatrix(List<String> lines) {
        var height = lines.size();
        var width = lines.stream().mapToInt(String::length).max().orElse(0);

        logger.debug("Converting parcel lines to matrix: {}x{}", height, width);

        char[][] matrix = new char[height][width];
        for (var i = 0; i < height; i++) {
            char[] row = lines.get(i).toCharArray();
            System.arraycopy(row, 0, matrix[i], 0, row.length);
        }
        return matrix;
    }

}