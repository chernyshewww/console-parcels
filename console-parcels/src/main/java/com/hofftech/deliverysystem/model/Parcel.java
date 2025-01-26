package com.hofftech.deliverysystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Parcel {
    private String name;
    private char symbol;
    private char[][] form;
    private List<int[]> coordinates;
    private int placedX;
    private int placedY;
    private boolean isPlaced;

    public Parcel(String name, char symbol, char[][] form) {
        this.name = name;
        this.symbol = symbol;
        this.form = form;
    }

    public Parcel(String name, char[][] form) {
        this.name = name;
        this.form = form;
    }

    public String getPlacedCoordinates() {
        if (!isPlaced) {
            return "Посылка не размещена.";
        }

        if (symbol == '\0') {
            return "Символ посылки не установлен.";
        }

        StringBuilder innerCoordinates = new StringBuilder();
        for (int i = 0; i < form.length; i++) {
            for (int j = 0; j < form[i].length; j++) {
                if (form[i][j] != 0) {
                    innerCoordinates.append(String.format("(%d, %d)", placedX + i, placedY + j)).append(", ");
                }
            }
        }

        if (innerCoordinates.length() > 2) {
            innerCoordinates.setLength(innerCoordinates.length() - 2);
        }
        return innerCoordinates.toString();
    }
}
