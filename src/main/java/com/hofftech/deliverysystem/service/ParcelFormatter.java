package com.hofftech.deliverysystem.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
public class ParcelFormatter {

    private final char[][] data;

    public ParcelFormatter(List<String> lines) {
        this.data = convertToMatrix(lines);
    }

    public char[][] convertToMatrix(List<String> lines) {
        var height = lines.size();
        var width = lines.stream().mapToInt(String::length).max().orElse(0);

        log.debug("Converting parcel lines to matrix: {}x{}", height, width);

        char[][] matrix = new char[height][width];
        for (var i = 0; i < height; i++) {
            char[] row = lines.get(i).toCharArray();
            System.arraycopy(row, 0, matrix[i], 0, row.length);
        }
        return clearCharArray(matrix);
    }

    private char[][] clearCharArray(char[][] matrix) {
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