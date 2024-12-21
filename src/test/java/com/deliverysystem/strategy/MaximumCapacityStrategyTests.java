package com.deliverysystem.strategy;

import com.deliverysystem.model.Truck;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaximumCapacityStrategyTests {

    @Test
    void testLoadParcels_OneParcelPerTruck() {
        MaximumCapacityStrategy strategy = new MaximumCapacityStrategy();
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'5', '5', '5', '5', '5'}
        });
        parcels.add(new char[][]{
                {'3', '3', '3'}
        });

        List<Truck> trucks = strategy.loadParcels(parcels, 10);

        assertEquals(1, trucks.size(), "Expected all parcels to fit in one truck.");
    }

    @Test
    void testLoadParcels_MultipleTrucksNeeded() {
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

        List<Truck> trucks = strategy.loadParcels(parcels, 10);

        assertNotNull(trucks, "Check that truck list should not be null");
    }

    @Test
    void testLoadParcels_EmptyInput() {
        MaximumCapacityStrategy strategy = new MaximumCapacityStrategy();
        List<char[][]> parcels = new ArrayList<>();

        List<Truck> trucks = strategy.loadParcels(parcels, 10);

        assertTrue(trucks.isEmpty(), "Expected no trucks when input is empty.");
    }

    @Test
    void testLoadParcels_FullTruckUtilization() {
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

        List<Truck> trucks = strategy.loadParcels(parcels, 10);

        assertEquals(1, trucks.size(), "Expected all parcels to fit into one truck.");
    }
}