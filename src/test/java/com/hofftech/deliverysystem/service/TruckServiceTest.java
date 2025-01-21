package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Truck;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        truckService = new TruckService();
    }

    @Test
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