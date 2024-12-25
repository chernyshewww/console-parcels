package com.hofftech.deliverysystem.model;

import java.util.Arrays;

public record Parcel(char[][] data) {

    @Override
    public String toString() {
        return Arrays.deepToString(data);
    }
}
