package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResultWriterServiceTests {

    @InjectMocks
    private ResultWriterService resultWriterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void writeTrucksToJson_GivenValidTrucks_ExpectedCorrectJsonFile() throws IOException {
        char[][] grid1 = {
                {'5', '5', '5', '5', '5'},
                {'6', '6', '6'},
                {'6', '6', '6'}
        };
        char[][] grid2 = {
                {'J', 'K', 'L'},
                {'M', 'N', 'O'},
                {'P', 'Q', 'R'}
        };

        Truck truck1 = new Truck();
        Truck truck2 = new Truck();

        truck1.setGrid(grid1);
        truck2.setGrid(grid2);

        List<Truck> trucks = Arrays.asList(truck1, truck2);

        File tempFile = File.createTempFile("trucks", ".json");
        String fileName = tempFile.getAbsolutePath();

        resultWriterService.writeTrucksToJson(trucks, fileName);

        File writtenFile = new File(fileName);
        assertThat(writtenFile.exists()).isTrue();

        String jsonContent = new String(Files.readAllBytes(Paths.get(fileName)));

        String expectedJson = """
                [
                  {
                    "Truck #1": [
                      "+55555+",
                      "+666+",
                      "+666+",
                      "++++++++"
                    ]
                  },
                  {
                    "Truck #2": [
                      "+JKL+",
                      "+MNO+",
                      "+PQR+",
                      "++++++++"
                    ]
                  }
                ]
                """;

        assertThat(jsonContent).isEqualToIgnoringNewLines(expectedJson);

        writtenFile.delete();
    }

    @Test
    void writeTrucksToJson_GivenInvalidFilePath_ExpectedIOException() {
        char[][] grid1 = {
                {'1', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };
        Truck truck1 = new Truck();
        truck1.setGrid(grid1);

        List<Truck> trucks = List.of(truck1);

        File tempFile = new File("/invalid.json");
        String fileName = tempFile.getAbsolutePath();

        assertThatThrownBy(() -> resultWriterService.writeTrucksToJson(trucks, fileName))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void writeTrucksToJson_GivenCustomFormattedTrucks_ExpectedCustomJsonFile() throws IOException {
        char[][] grid1 = {
                {'1', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };
        Truck truck1 = new Truck();
        truck1.setGrid(grid1);

        List<Truck> trucks = List.of(truck1);

        File tempFile = File.createTempFile("custom_trucks", ".json");
        String fileName = tempFile.getAbsolutePath();

        resultWriterService.writeTrucksToJson(trucks, fileName);

        File writtenFile = new File(fileName);
        assertThat(writtenFile.exists()).isTrue();

        String jsonContent = new String(Files.readAllBytes(Paths.get(fileName)));

        String expectedJson = """
                [
                  {
                    "Truck #1": [
                      "+1  +",
                      "+   +",
                      "+   +",
                      "++++++++"
                    ]
                  }
                ]
                """;

        assertThat(jsonContent).isEqualToIgnoringNewLines(expectedJson);

        writtenFile.delete();
    }
}
