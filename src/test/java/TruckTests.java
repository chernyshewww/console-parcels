package org.example;

import com.deliverysystem.Parcel;
import com.deliverysystem.Truck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckTests {

    @Test
    void testCanPlace_ValidPosition() {
        // Arrange
        Truck truck = new Truck();
        Parcel parcel = new Parcel(List.of(
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
        Truck truck = new Truck();
        Parcel parcel = new Parcel(List.of(
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
        Truck truck = new Truck();
        Parcel parcel1 = new Parcel(List.of(
                "66",
                "66"
        ));
        Parcel parcel2 = new Parcel(List.of(
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