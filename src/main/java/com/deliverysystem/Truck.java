package com.deliverysystem;

import java.util.Arrays;

// Abstraction for trucks
public class Truck {
    public static final int WIDTH = 6;
    public static final int HEIGHT = 6;
    public static final char EMPTY_CELL = ' ';

    private final char[][] grid;

    public Truck() {
        this.grid = new char[HEIGHT][WIDTH];
        for (char[] row : grid) {
            Arrays.fill(row, ' ');
        }
    }

    public boolean canPlace(Parcel parcel, int row, int col) {
        char[][] parcelData = parcel.getData();
        var parcelHeight = parcelData.length;
        var parcelWidth = parcelData[0].length;

        if (row + parcelHeight > HEIGHT || col + parcelWidth > WIDTH) {
            return false;
        }

        for (var i = 0; i < parcelHeight; i++) {
            for (var j = 0; j < parcelWidth; j++) {
                if (parcelData[i][j] != EMPTY_CELL && grid[row + i][col + j] != EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

    public void place(Parcel parcel, int row, int col) {
        char[][] parcelData = parcel.getData();
        for (var i = 0; i < parcelData.length; i++) {
            for (var j = 0; j < parcelData[0].length; j++) {
                if (parcelData[i][j] != EMPTY_CELL) {
                    grid[row + i][col + j] = parcelData[i][j];
                }
            }
        }
    }

    public void print() {
        System.out.println("+      +");
        for (char[] row : grid) {
            System.out.print("+");
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println("+");
        }
        System.out.println("++++++++");
        System.out.println();

    }

    public char getCell(int row, int col) {
        return grid[row][col];
    }
}
