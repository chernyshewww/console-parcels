package com.deliverysystem.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckServiceTests {

    @Test
    void testCanPlace_ValidPosition() {
        TruckService truck = new TruckService();
        ParcelService parcel = new ParcelService(List.of(
                "66",
                "66"
        ));

        boolean result = truck.canPlace(parcel, 0, 0);

        assertTrue(result, "The parcel should be placeable at (0, 0) in an empty truck.");
    }

    @Test
    void testCanPlace_OutOfBounds() {
        TruckService truck = new TruckService();
        ParcelService parcel = new ParcelService(List.of(
                "66",
                "66"
        ));

        boolean result = truck.canPlace(parcel, 5, 5);

        assertFalse(result, "The parcel should not be placeable out of truck bounds.");
    }

    @Test
    void testCanPlace_OccupiedSpace() {
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

        boolean result = truck.canPlace(parcel2, 0, 0);

        assertFalse(result, "The parcel should not be placeable on an occupied space.");
    }
}