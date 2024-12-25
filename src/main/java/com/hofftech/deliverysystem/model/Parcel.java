package com.hofftech.deliverysystem.model;

import java.util.Arrays;
import java.util.Objects;

public record Parcel(char[][] data) {

    @Override
    public String toString() {
        return Arrays.deepToString(data);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return Objects.deepEquals(data, parcel.data);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(data);
    }
}
