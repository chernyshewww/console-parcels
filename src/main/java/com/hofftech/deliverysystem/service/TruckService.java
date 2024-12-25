package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;

import static com.hofftech.deliverysystem.constants.Constant.EMPTY_CELL;

public class TruckService {

    public boolean canPlace(Parcel parcel, Truck truck, int row, int col) {
        char[][] parcelData = parcel.data();
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

    public void place(Parcel parcel, Truck truck, int row, int col) {
        char[][] parcelData = parcel.data();
        for (int i = 0; i < parcelData.length; i++) {
            for (int j = 0; j < parcelData[0].length; j++) {
                if (parcelData[i][j] != EMPTY_CELL) {
                    truck.insertIntoGrid(row + i, col + j,parcelData[i][j]);
                }
            }
        }
    }

    public void printTruck(Truck truck, int truckNumber) {
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