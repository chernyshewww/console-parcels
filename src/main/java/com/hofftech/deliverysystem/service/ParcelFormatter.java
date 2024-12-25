package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ParcelFormatter {

    public Parcel convertToMatrix(List<String> lines) {
        var height = lines.size();
        var width = lines.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        log.debug("Converting parcel lines to matrix: {}x{}", height, width);

        char[][] matrix = new char[height][width];
        for (var i = 0; i < height; i++) {
            char[] row = lines.get(i).toCharArray();
            System.arraycopy(row, 0, matrix[i], 0, row.length);
        }
        return clearCharArray(matrix);
    }

    private Parcel clearCharArray(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == '\u0000') {
                    matrix[i][j] = ' ';
                }
            }
        }
        return new Parcel(matrix);
    }
}