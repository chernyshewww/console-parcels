package com.hofftech.deliverysystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

import static com.hofftech.deliverysystem.constants.Constant.*;

@Getter
@Setter
public class Truck{

    private final int height;
    private final int width;
    private char[][] grid;

    public Truck() {
        this.height = TRUCK_HEIGHT;
        this.width = TRUCK_WIDTH;
        this.grid = new char[TRUCK_HEIGHT][TRUCK_WIDTH];
        fillGridWithEmptyCells();
    }
    public void insertIntoGrid(int row, int col, char value) {
        this.grid[row][col] = value;
    }

    private void fillGridWithEmptyCells() {
        for (char[] row : grid) {
            Arrays.fill(row, EMPTY_CELL);
        }
    }
}
