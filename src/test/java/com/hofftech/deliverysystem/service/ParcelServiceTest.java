package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.repository.ParcelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParcelServiceTest {

    @InjectMocks
    private ParcelService parcelService;

    @Mock
    private ParcelRepository parcelRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUnloadParcelsFromTrucks() {
        Truck truck = new Truck(5, 5);
        Parcel parcel = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        truck.getParcels().add(parcel);

        List<Parcel> parcels = parcelService.unloadParcelsFromTrucks(Collections.singletonList(truck));

        assertThat(parcels).hasSize(1);
        assertThat(parcels.getFirst().getName()).isEqualTo("Parcel1");
    }

    @Test
    void testTryPlaceParcel_Success() {
        Truck truck = new Truck(5, 5);
        Parcel parcel = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});

        boolean result = parcelService.tryPlaceParcel(truck, parcel);

        assertThat(result).isTrue();
        assertThat(truck.getParcels()).contains(parcel);
    }
}
