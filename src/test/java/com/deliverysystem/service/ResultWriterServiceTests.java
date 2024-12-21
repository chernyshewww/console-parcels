package com.deliverysystem.service;

import com.deliverysystem.model.Truck;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ResultWriterServiceTests {

    @Test
    void testWriteTrucksToJson() throws IOException {
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

        ResultWriterService.writeTrucksToJson(trucks, fileName);

        File writtenFile = new File(fileName);
        assertThat(writtenFile.exists()).isTrue();

        String jsonContent = new String(Files.readAllBytes(Paths.get(fileName)));

        String expectedJson = "[\n" +
                "  {\n" +
                "    \"Truck #1\": [\n" +
                "      \"+55555+\",\n" +
                "      \"+666+\",\n" +
                "      \"+666+\",\n" +
                "      \"++++++++\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Truck #2\": [\n" +
                "      \"+JKL+\",\n" +
                "      \"+MNO+\",\n" +
                "      \"+PQR+\",\n" +
                "      \"++++++++\"\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        assertThat(jsonContent).isEqualTo(expectedJson);

        writtenFile.delete();
    }

    @Test
    void testWriteTrucksToJson_withIOException() {
        char[][] grid1 = {
                {'1', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };
        Truck truck1 = new Truck();
        truck1.setGrid(grid1);
        List<Truck> trucks = List.of(truck1);

        // Making an invalidPath
        File tempFile = new File("/invalid.json");
        String fileName = tempFile.getAbsolutePath();

        assertThatThrownBy(() -> ResultWriterService.writeTrucksToJson(trucks, fileName))
                .isInstanceOf(IOException.class);
    }

    @Test
    void testWriteCustomFormattedTrucks() throws IOException {
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

        ResultWriterService.writeTrucksToJson(trucks, fileName);

        File writtenFile = new File(fileName);
        assertThat(writtenFile.exists()).isTrue();

        String jsonContent = new String(Files.readAllBytes(Paths.get(fileName)));

        String expectedJson = "[\n" +
                "  {\n" +
                "    \"Truck #1\": [\n" +
                "      \"+1  +\",\n" +
                "      \"+   +\",\n" +
                "      \"+   +\",\n" +
                "      \"++++++++\"\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        assertThat(jsonContent).isEqualTo(expectedJson);

        writtenFile.delete();
    }
}