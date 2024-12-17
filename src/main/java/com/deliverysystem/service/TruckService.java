package com.deliverysystem.service;

import java.util.Arrays;

public class TruckService {
    public static final int WIDTH = 6;
    public static final int HEIGHT = 6;
    public static final char EMPTY_CELL = ' ';

    private final char[][] grid;

    public TruckService() {
        this.grid = new char[HEIGHT][WIDTH];
        for (char[] row : grid) {
            Arrays.fill(row, EMPTY_CELL);
        }
    }

    public boolean canPlace(ParcelService parcel, int row, int col) {
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

    public void place(ParcelService parcel, int row, int col) {
        char[][] parcelData = parcel.getData();
        for (int i = 0; i < parcelData.length; i++) {
            for (int j = 0; j < parcelData[0].length; j++) {
                if (parcelData[i][j] != EMPTY_CELL) {
                    grid[row + i][col + j] = parcelData[i][j];
                }
            }
        }
    }

    public char[][] getGrid() {
        return grid;
    }

    public void printTruck(int truckNumber) {
        System.out.println("Truck " + truckNumber + ":");
        System.out.println("+      +");
        for (char[] row : grid) {
            System.out.print("+");
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println("+");
        }
        System.out.println("++++++++");
    }
}