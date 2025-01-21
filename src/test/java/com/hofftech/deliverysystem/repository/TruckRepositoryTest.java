package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.exception.TruckFileReadException;
import com.hofftech.deliverysystem.model.Truck;
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
class TruckRepositoryTest {

    @InjectMocks
    private TruckRepository truckRepository;

    @Test
    void testLoadTrucksFromFile_ValidFile() throws IOException, TruckFileReadException {
        File tempFile = File.createTempFile("trucks", ".json");
        tempFile.deleteOnExit();
        String jsonContent = "[{\"width\":3,\"height\":2},{\"width\":4,\"height\":5}]";
        java.nio.file.Files.write(tempFile.toPath(), jsonContent.getBytes());

        List<Truck> trucks = truckRepository.loadTrucksFromFile(tempFile.getAbsolutePath());

        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getWidth()).isEqualTo(3);
        assertThat(trucks.get(0).getHeight()).isEqualTo(2);
        assertThat(trucks.get(1).getWidth()).isEqualTo(4);
        assertThat(trucks.get(1).getHeight()).isEqualTo(5);
    }

    @Test
    void testLoadTrucksFromFile_InvalidFile() {
        String invalidFileName = "nonexistent_file.json";

        assertThatThrownBy(() -> truckRepository.loadTrucksFromFile(invalidFileName))
                .isInstanceOf(TruckFileReadException.class)
                .hasMessageContaining("Ошибка при чтении файла грузовиков: " + invalidFileName);
    }
}
