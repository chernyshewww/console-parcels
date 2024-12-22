package com.deliverysystem.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ParcelServiceTests {

    private static final Logger logger = LoggerFactory.getLogger(ParcelService.class);

    @Test
    void shouldConvertLinesToMatrix() {
        List<String> lines = List.of(
                "55555",
                "4444",
                "333"
        );

        ParcelService parcelService = new ParcelService(lines);

        char[][] data = parcelService.getData();

        assertThat(data.length).isEqualTo(3);
        assertThat(data[0].length).isEqualTo(5);

        assertThat(data[0]).containsExactly('5', '5', '5', '5', '5');
        assertThat(data[1]).containsExactly('4', '4', '4', '4', ' ');
        assertThat(data[2]).containsExactly('3', '3', '3', ' ', ' ');
    }

    @Test
    void shouldHandleEmptyList() {
        List<String> lines = List.of();

        ParcelService parcelService = new ParcelService(lines);

        char[][] data = parcelService.getData();
        assertThat(data).isEmpty();
    }

    @Test
    void shouldHandleUnevenRowLengths() {
        List<String> lines = List.of(
                "ABCDE",
                "12",
                "!!@"
        );

        ParcelService parcelService = new ParcelService(lines);

        char[][] data = parcelService.getData();

        assertThat(data.length).isEqualTo(3);
        assertThat(data[0].length).isEqualTo(5);

        assertThat(data[1]).containsExactly('1', '2', ' ', ' ', ' ');
        assertThat(data[2]).containsExactly('!', '!', '@', ' ', ' ');
    }
}
