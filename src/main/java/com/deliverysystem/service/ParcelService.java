package com.deliverysystem.service;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
public class ParcelService {
    private static final Logger logger = LoggerFactory.getLogger(ParcelService.class);
    private final char[][] data;

    public ParcelService(List<String> lines) {
        this.data = convertToMatrix(lines);
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
        return clearCharArray(matrix);
    }

    public char[][] clearCharArray(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == '\u0000') {
                    matrix[i][j] = ' ';
                }
            }
        }
        return matrix;
    }
}