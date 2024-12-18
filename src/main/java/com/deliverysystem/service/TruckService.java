package com.deliverysystem.service;

import com.deliverysystem.model.Truck;

public class TruckService {
    public static final char EMPTY_CELL = ' ';

    private final Truck truck;

    public TruckService(Truck truck) {
        this.truck = truck;
    }

    public boolean canPlace(ParcelService parcel, int row, int col) {
        char[][] parcelData = parcel.getData();
        var parcelHeight = parcelData.length;
        var parcelWidth = parcelData[0].length;

        if (row + parcelHeight > truck.getHeight() || col + parcelWidth > truck.getWidth()) {
            return false;
        }

        for (var i = 0; i < parcelHeight; i++) {
            for (var j = 0; j < parcelWidth; j++) {
                if (parcelData[i][j] != EMPTY_CELL && truck.getGrid()[row + i][col + j] != EMPTY_CELL) {
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
                    truck.insertIntoGrid(row + i, col + j,parcelData[i][j]);
                }
            }
        }
    }

    public void printTruck(int truckNumber) {
        System.out.println("Truck " + truckNumber + ":");
        System.out.println("+      +");
        for (char[] row : truck.getGrid()) {
            System.out.print("+");
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println("+");
        }
        System.out.println("++++++++");
    }

}