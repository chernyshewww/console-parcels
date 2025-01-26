package com.hofftech.deliverysystem.util;

import org.springframework.stereotype.Service;

@Service
public class TruckGridHelper {
    public String parseGridToString(char[][] grid) {
        StringBuilder gridString = new StringBuilder();

        for (char[] row : grid) {
            for (char cell : row) {
                // Replace empty characters with '-'
                gridString.append(cell == '\0' ? '-' : cell);
            }
            // Add a newline after each row
            gridString.append("\n");
        }

        // Remove the trailing newline for clean output
        if (gridString.length() > 0) {
            gridString.setLength(gridString.length() - 1);
        }

        return gridString.toString();
    }
}
