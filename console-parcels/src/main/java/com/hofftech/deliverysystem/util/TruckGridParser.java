package com.hofftech.deliverysystem.util;

import org.springframework.stereotype.Service;

@Service
public class TruckGridParser {
    public String parse(char[][] grid) {
        StringBuilder gridString = new StringBuilder();

        for (char[] row : grid) {
            for (char cell : row) {
                gridString.append(cell == '\0' ? '-' : cell);
            }
            gridString.append("\n");
        }

        if (gridString.length() > 0) {
            gridString.setLength(gridString.length() - 1);
        }

        return gridString.toString();
    }
}
