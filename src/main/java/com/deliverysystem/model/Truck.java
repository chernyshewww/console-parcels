package com.deliverysystem.model;

import com.deliverysystem.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class Truck {
    private int height;
    private int width;

    private char[][] grid;

    public Truck() {
        this.height = Constants.TRUCK_HEIGHT;
        this.width = Constants.TRUCK_WIDTH;
        this.grid = new char[Constants.TRUCK_HEIGHT][Constants.TRUCK_WIDTH];
        for (char[] row : grid) {
            Arrays.fill(row, Constants.EMPTY_CELL);
        }
    }

    public void insertIntoGrid(int row, int col, char value) {
        this.grid[row][col] = value;
    }
}
