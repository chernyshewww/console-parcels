package org.example;

import com.deliverysystem.service.ParcelService;
import com.deliverysystem.service.TruckService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckTests {

    @Test
    void testCanPlace_ValidPosition() {
        // Arrange
        TruckService truck = new TruckService();
        ParcelService parcel = new ParcelService(List.of(
                "66",
                "66"
        ));

        // Act
        boolean result = truck.canPlace(parcel, 0, 0);

        // Assert
        assertTrue(result, "The parcel should be placeable at (0, 0) in an empty truck.");
    }

    @Test
    void testCanPlace_OutOfBounds() {
        // Arrange
        TruckService truck = new TruckService();
        ParcelService parcel = new ParcelService(List.of(
                "66",
                "66"
        ));

        // Act
        boolean result = truck.canPlace(parcel, 5, 5);

        // Assert
        assertFalse(result, "The parcel should not be placeable out of truck bounds.");
    }

    @Test
    void testCanPlace_OccupiedSpace() {
        // Arrange
        TruckService truck = new TruckService();
        ParcelService parcel1 = new ParcelService(List.of(
                "66",
                "66"
        ));
        ParcelService parcel2 = new ParcelService(List.of(
                "77",
                "77"
        ));

        truck.place(parcel1, 0, 0);

        // Act
        boolean result = truck.canPlace(parcel2, 0, 0);

        // Assert
        assertFalse(result, "The parcel should not be placeable on an occupied space.");
    }
}