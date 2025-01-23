package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Truck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TruckServiceTest {

    @InjectMocks
    private TruckService truckService;

    @Test
    @DisplayName("Должен корректно генерировать представление для кузова грузовика")
    void testGenerateTruckView() {
        char[][] grid = {
                {'A', '\0', 'B'},
                {'\0', 'C', '\0'},
                {'D', '\0', 'E'}
        };
        Truck truck = new Truck(3, 3);
        truck.setGrid(grid);

        String view = truckService.generateTruckView(truck);

        String expectedView = "A B\n C \nD E\n";
        assertThat(view).isEqualTo(expectedView);
    }

    @Test
    @DisplayName("Должен корректно парсить размеры грузовиков из строки")
    void testParseTruckSizes_ValidInput() {
        String trucksText = "3x2\\n4x5\\n2x2";

        List<Truck> trucks = truckService.parseTruckSizes(trucksText);

        assertThat(trucks).hasSize(3);
        assertThat(trucks.get(0).getWidth()).isEqualTo(3);
        assertThat(trucks.get(0).getHeight()).isEqualTo(2);
        assertThat(trucks.get(1).getWidth()).isEqualTo(4);
        assertThat(trucks.get(1).getHeight()).isEqualTo(5);
        assertThat(trucks.get(2).getWidth()).isEqualTo(2);
        assertThat(trucks.get(2).getHeight()).isEqualTo(2);
    }

    @Test
    @DisplayName("Должен корректно парсить размеры грузовиков с некорректными значениями")
    void testParseTruckSizes_InvalidInput() {
        String trucksText = "3x2\\ninvalid\\n4x5";

        List<Truck> trucks = truckService.parseTruckSizes(trucksText);

        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getWidth()).isEqualTo(3);
        assertThat(trucks.get(0).getHeight()).isEqualTo(2);
        assertThat(trucks.get(1).getWidth()).isEqualTo(4);
        assertThat(trucks.get(1).getHeight()).isEqualTo(5);
    }
}
