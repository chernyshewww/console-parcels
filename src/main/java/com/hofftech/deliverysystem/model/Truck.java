package com.hofftech.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hofftech.deliverysystem.constants.Constant;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
@Setter
public class Truck {

    private List<Parcel> parcels = new ArrayList<>();
    private String truckType;
    private int height;
    private int width;
    private char[][] grid;

    public Truck(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        fillGridWithEmptyCells();
    }

    public void insertIntoGrid(int row, int col, char value) {
        this.grid[row][col] = value;
    }

    private void fillGridWithEmptyCells() {
        for (char[] row : grid) {
            Arrays.fill(row, (char) Constant.EMPTY_CELL.getValue());
        }
    }

//    @JsonCreator
//    public Truck(@JsonProperty("truck_type") String truckType, @JsonProperty("parcels") List<Parcel> parcels) {
//        this.truckType = truckType;
//        this.parcels = parcels;
//    }
}
