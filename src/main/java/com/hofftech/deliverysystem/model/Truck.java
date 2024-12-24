package com.hofftech.deliverysystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class Truck{

    public static final int TRUCK_WIDTH = 6;
    public static final int TRUCK_HEIGHT = 6;
    public static final char EMPTY_CELL = ' ';
    private final int height;
    private final int width;

    private char[][] grid;

    public Truck() {
        this.height = TRUCK_HEIGHT;
        this.width = TRUCK_WIDTH;
        this.grid = new char[TRUCK_HEIGHT][TRUCK_WIDTH];
        fillGridWithEmptyCells();
    }

    private void fillGridWithEmptyCells() {
        for (char[] row : grid) {
            Arrays.fill(row, EMPTY_CELL);
        }
    }

    public void insertIntoGrid(int row, int col, char value) {
        this.grid[row][col] = value;
    }
}
