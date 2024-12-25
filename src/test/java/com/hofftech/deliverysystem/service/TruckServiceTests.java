package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TruckServiceTests {

    @Mock
    private ParcelFormatter parcelFormatter;

    @BeforeEach
    void setUp() {
        parcelFormatter = new ParcelFormatter();
    }

    @Test
    void canPlace_GivenValidPosition_ExpectedTrue() {
        TruckService truckService = new TruckService();

        Parcel parcel= parcelFormatter.convertToMatrix(List.of(
                "66",
                "66"
        ));

        boolean result = truckService.canPlace(parcel, new Truck(), 0, 0);

        assertThat(result)
                .isTrue()
                .withFailMessage("The parcel should be placeable at (0, 0) in an empty truck.");
    }

    @Test
    void canPlace_GivenOutOfBoundsPosition_ExpectedFalse() {
        TruckService truckService = new TruckService();
        Parcel parcel= parcelFormatter.convertToMatrix(List.of(
                "66",
                "66"
        ));

        boolean result = truckService.canPlace(parcel, new Truck(), 5, 5);

        assertThat(result)
                .isFalse()
                .withFailMessage("The parcel should not be placeable out of truck bounds.");
    }

    @Test
    void canPlace_GivenOccupiedSpace_ExpectedFalse() {
        TruckService truckService = new TruckService();
        Parcel parcel1= parcelFormatter.convertToMatrix(List.of(
                "66",
                "66"
        ));
        Parcel parcel2 = parcelFormatter.convertToMatrix(List.of(
                "77",
                "77"
        ));

        var truck = new Truck();
        truckService.place(parcel1, truck, 0, 0);

        boolean result = truckService.canPlace(parcel2, truck, 0, 0);

        assertThat(result)
                .isFalse()
                .withFailMessage("The parcel should not be placeable on an occupied space.");
    }
}
