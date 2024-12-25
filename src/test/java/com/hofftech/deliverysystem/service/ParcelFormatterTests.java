package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParcelFormatterTests {

    @Test
    void convertLinesToMatrix_GivenValidLines_ExpectedCorrectMatrix() {
        List<String> lines = List.of(
                "55555",
                "4444",
                "333"
        );

        ParcelFormatter parcelFormatter = new ParcelFormatter();

        Parcel parcel = parcelFormatter.convertToMatrix(lines);
        char[][] data = parcel.data();

        assertThat(data.length).isEqualTo(3);
        assertThat(data[0].length).isEqualTo(5);

        assertThat(data[0]).containsExactly('5', '5', '5', '5', '5');
        assertThat(data[1]).containsExactly('4', '4', '4', '4', ' ');
        assertThat(data[2]).containsExactly('3', '3', '3', ' ', ' ');
    }

    @Test
    void handleEmptyList_GivenEmptyList_ExpectedEmptyMatrix() {
        List<String> lines = List.of();

        ParcelFormatter parcelFormatter = new ParcelFormatter();
        Parcel parcel = parcelFormatter.convertToMatrix(lines);


        char[][] data = parcel.data();
        assertThat(data).isEmpty();
    }

    @Test
    void handleUnevenRowLengths_GivenUnevenRows_ExpectedMatrixWithPadding() {
        List<String> lines = List.of(
                "ABCDE",
                "12",
                "!!@"
        );

        ParcelFormatter parcelFormatter = new ParcelFormatter();
        Parcel parcel = parcelFormatter.convertToMatrix(lines);

        char[][] data = parcel.data();

        assertThat(data).hasNumberOfRows(3);
        assertThat(data[0]).hasSize(5);

        assertThat(data[1]).containsExactly('1', '2', ' ', ' ', ' ');
        assertThat(data[2]).containsExactly('!', '!', '@', ' ', ' ');
    }
}
