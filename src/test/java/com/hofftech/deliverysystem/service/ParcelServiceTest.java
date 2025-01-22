package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.repository.impl.ParcelRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ParcelServiceTest {

    @InjectMocks
    private ParcelService parcelService;

    @Mock
    private ParcelRepositoryImpl parcelRepository;

    @Test
    @DisplayName("Должен выгружать посылки из грузовиков")
    void testUnloadParcelsFromTrucks() {
        Truck truck = new Truck(5, 5);
        Parcel parcel = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        truck.getParcels().add(parcel);

        List<Parcel> parcels = parcelService.unloadParcelsFromTrucks(Collections.singletonList(truck));

        assertThat(parcels).hasSize(1);
        assertThat(parcels.get(0).getName()).isEqualTo("Parcel1");
    }

    @Test
    @DisplayName("Должен успешно попытаться разместить посылку")
    void testTryPlaceParcel_Success() {
        Truck truck = new Truck(5, 5);
        Parcel parcel = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});

        boolean result = parcelService.tryPlaceParcel(truck, parcel);

        assertThat(result).isTrue();
        assertThat(truck.getParcels()).contains(parcel);
    }
}
