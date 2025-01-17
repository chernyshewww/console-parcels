package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.exception.TruckFileReadException;
import com.hofftech.deliverysystem.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class TruckServiceTest {

    @InjectMocks
    private TruckService truckService;

    @BeforeEach
    void setUp() {
        truckService = new TruckService();
    }

    @Test
    void testGenerateTruckView() {
        // Arrange
        char[][] grid = {
                {'A', '\0', 'B'},
                {'\0', 'C', '\0'},
                {'D', '\0', 'E'}
        };
        Truck truck = new Truck(3, 3);
        truck.setGrid(grid);

        // Act
        String view = truckService.generateTruckView(truck);

        // Assert
        String expectedView = "A B\n C \nD E\n";
        assertThat(view).isEqualTo(expectedView);
    }

    @Test
    void testParseTruckSizes_ValidInput() {
        // Arrange
        String trucksText = "3x2\\n4x5\\n2x2";

        // Act
        List<Truck> trucks = truckService.parseTruckSizes(trucksText);

        // Assert
        assertThat(trucks).hasSize(3);
        assertThat(trucks.get(0).getWidth()).isEqualTo(3);
        assertThat(trucks.get(0).getHeight()).isEqualTo(2);
        assertThat(trucks.get(1).getWidth()).isEqualTo(4);
        assertThat(trucks.get(1).getHeight()).isEqualTo(5);
        assertThat(trucks.get(2).getWidth()).isEqualTo(2);
        assertThat(trucks.get(2).getHeight()).isEqualTo(2);
    }

    @Test
    void testParseTruckSizes_InvalidInput() {
        // Arrange
        String trucksText = "3x2\\ninvalid\\n4x5";

        // Act
        List<Truck> trucks = truckService.parseTruckSizes(trucksText);

        // Assert
        assertThat(trucks).hasSize(2); // Only valid dimensions are parsed
        assertThat(trucks.get(0).getWidth()).isEqualTo(3);
        assertThat(trucks.get(0).getHeight()).isEqualTo(2);
        assertThat(trucks.get(1).getWidth()).isEqualTo(4);
        assertThat(trucks.get(1).getHeight()).isEqualTo(5);
    }

    @Test
    void testLoadTrucksFromFile_ValidFile() throws IOException, TruckFileReadException {
        // Arrange
        File tempFile = File.createTempFile("trucks", ".json");
        tempFile.deleteOnExit();
        String jsonContent = "[{\"width\":3,\"height\":2},{\"width\":4,\"height\":5}]";
        java.nio.file.Files.write(tempFile.toPath(), jsonContent.getBytes());

        // Act
        List<Truck> trucks = truckService.loadTrucksFromFile(tempFile.getAbsolutePath());

        // Assert
        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getWidth()).isEqualTo(3);
        assertThat(trucks.get(0).getHeight()).isEqualTo(2);
        assertThat(trucks.get(1).getWidth()).isEqualTo(4);
        assertThat(trucks.get(1).getHeight()).isEqualTo(5);
    }

    @Test
    void testLoadTrucksFromFile_InvalidFile() {
        // Arrange
        String invalidFileName = "nonexistent_file.json";

        // Act & Assert
        assertThatThrownBy(() -> truckService.loadTrucksFromFile(invalidFileName))
                .isInstanceOf(TruckFileReadException.class)
                .hasMessageContaining("Ошибка при чтении файла грузовиков: " + invalidFileName);
    }
}