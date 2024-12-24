//package com.hofftech.deliverysystem.service;
//
//import com.hofftech.deliverysystem.model.Truck;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Disabled
//class TruckServiceTests {
//
//    @Test
//    void canPlace_GivenValidPosition_ExpectedTrue() {
//        TruckService truckService = new TruckService(new Truck());
//        ParcelFormatter parcel = new ParcelFormatter(List.of(
//                "66",
//                "66"
//        ));
//
//        boolean result = truckService.canPlace(parcel, 0, 0);
//
//        assertTrue(result, "The parcel should be placeable at (0, 0) in an empty truck.");
//    }
//
//    @Test
//    void canPlace_GivenOutOfBoundsPosition_ExpectedFalse() {
//        TruckService truckService = new TruckService(new Truck());
//        ParcelFormatter parcel = new ParcelFormatter(List.of(
//                "66",
//                "66"
//        ));
//
//        boolean result = truckService.canPlace(parcel, 5, 5);
//
//        assertFalse(result, "The parcel should not be placeable out of truck bounds.");
//    }
//
//    @Test
//    void canPlace_GivenOccupiedSpace_ExpectedFalse() {
//        TruckService truckService = new TruckService(new Truck());
//        ParcelFormatter parcel1 = new ParcelFormatter(List.of(
//                "66",
//                "66"
//        ));
//        ParcelFormatter parcel2 = new ParcelFormatter(List.of(
//                "77",
//                "77"
//        ));
//
//        truckService.place(parcel1, 0, 0);
//
//        boolean result = truckService.canPlace(parcel2, 0, 0);
//
//        assertFalse(result, "The parcel should not be placeable on an occupied space.");
//    }
//}
