package org.example;

import com.deliverysystem.ParcelLoader;
import com.deliverysystem.Truck;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParcelLoaderTests {

    @Test
    void testLoadParcels_Strategy2_OneParcelPerTruck() {
        // Arrange
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'6', '6'},
                {'6', '6'}
        });
        parcels.add(new char[][]{
                {'7', '7', '7'},
                {'7', '7', '7'}
        });

        // Act
        List<Truck> trucks = ParcelLoader.loadParcels(parcels, 2);

        // Assert
        assertEquals(2, trucks.size(), "Strategy 2 should use one truck per parcel.");
    }

    @Test
    void testLoadParcels_EmptyList() {
        // Arrange
        List<char[][]> parcels = new ArrayList<>();

        // Act
        List<Truck> trucks = ParcelLoader.loadParcels(parcels, 1);

        // Assert
        assertTrue(trucks.isEmpty(), "No trucks should be created for an empty list of parcels.");
    }
}