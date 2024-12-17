package org.example;

import com.deliverysystem.service.TruckService;
import com.deliverysystem.strategy.MaximumCapacityStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaximumCapacityStrategyTests {

    @Test
    void testLoadParcels_OneParcelPerTruck() {
        // Arrange
        MaximumCapacityStrategy strategy = new MaximumCapacityStrategy();
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'5', '5', '5', '5', '5'}
        });
        parcels.add(new char[][]{
                {'3', '3', '3'}
        });

        // Act
        List<TruckService> trucks = strategy.loadParcels(parcels);

        // Assert
        assertEquals(1, trucks.size(), "Expected all parcels to fit in one truck.");
        TruckService truck = trucks.getFirst();
        truck.printTruck(1);
    }

    @Test
    void testLoadParcels_MultipleTrucksNeeded() {
        // Arrange
        MaximumCapacityStrategy strategy = new MaximumCapacityStrategy();
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'5', '5', '5', '5', '5'}
        });
        parcels.add(new char[][]{
                {'4', '4', '4', '4'}
        });
        parcels.add(new char[][]{
                {'3', '3', '3'}
        });

        // Act
        List<TruckService> trucks = strategy.loadParcels(parcels);

        // Assert
        trucks.get(0).printTruck(1);
    }

    @Test
    void testLoadParcels_EmptyInput() {
        // Arrange
        MaximumCapacityStrategy strategy = new MaximumCapacityStrategy();
        List<char[][]> parcels = new ArrayList<>();

        // Act
        List<TruckService> trucks = strategy.loadParcels(parcels);

        // Assert
        assertTrue(trucks.isEmpty(), "Expected no trucks when input is empty.");
    }

    @Test
    void testLoadParcels_FullTruckUtilization() {
        // Arrange
        MaximumCapacityStrategy strategy = new MaximumCapacityStrategy();
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'8', '8', '8', '8'}
        });
        parcels.add(new char[][]{
                {'8', '8', '8', '8'}
        });
        parcels.add(new char[][]{
                {'6', '6', '6'}
        });
        parcels.add(new char[][]{
                {'6', '6'}
        });

        // Act
        List<TruckService> trucks = strategy.loadParcels(parcels);

        // Assert
        assertEquals(1, trucks.size(), "Expected all parcels to fit into one truck.");
        TruckService truck = trucks.get(0);
        truck.printTruck(1);
    }
}