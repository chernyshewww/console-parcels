package com.hofftech.deliverysystem.model;

import java.util.Arrays;

public class Parcel {
    private final char[][] data;

    public Parcel(char[][] data) {
        this.data = data;
    }

    public char[][] getData() {
        return data;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(data);
    }
}
