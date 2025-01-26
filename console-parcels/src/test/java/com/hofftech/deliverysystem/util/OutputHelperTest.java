package com.hofftech.deliverysystem.util;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OutputHelperTest {

    @InjectMocks
    private OutputHelper outputHelper;

    @Test
    @DisplayName("Должен корректно создавать данные о грузовике")
    void testCreateTruckData() {
        Parcel parcel1 = createParcel("Parcel1", 0, 0, new char[][]{
                {'1', '0'},
                {'1', '1'}
        });

        Parcel parcel2 = createParcel("Parcel2", 1, 1, new char[][]{
                {'1'}
        });

        Truck truck = new Truck(5, 5);
        truck.setParcels(List.of(parcel1, parcel2));
        truck.setWidth(5);
        truck.setHeight(5);

        Map<String, Object> truckData = outputHelper.createTruckData(truck);

        assertThat(truckData).isNotNull();
        assertThat(truckData).containsKeys("truck_type", "parcels");
        assertThat(truckData.get("truck_type")).isEqualTo("5x5");

        List<Map<String, Object>> parcelsData = (List<Map<String, Object>>) truckData.get("parcels");
        assertThat(parcelsData).hasSize(2);

        Map<String, Object> parcel1Data = parcelsData.get(0);
        assertThat(parcel1Data).containsEntry("name", "Parcel1");
        assertThat(parcel1Data).containsKey("coordinates");

        List<List<Integer>> parcel1Coordinates = (List<List<Integer>>) parcel1Data.get("coordinates");
        assertThat(parcel1Coordinates).containsExactlyInAnyOrder(
                List.of(0, 0),
                List.of(1, 0),
                List.of(1, 1)
        );

        Map<String, Object> parcel2Data = parcelsData.get(1);
        assertThat(parcel2Data).containsEntry("name", "Parcel2");
        assertThat(parcel2Data).containsKey("coordinates");

        List<List<Integer>> parcel2Coordinates = (List<List<Integer>>) parcel2Data.get("coordinates");
        assertThat(parcel2Coordinates).containsExactlyInAnyOrder(
                List.of(1, 1)
        );
    }

    private Parcel createParcel(String name, int placedX, int placedY, char[][] form) {
        Parcel parcel = new Parcel();
        parcel.setName(name);
        parcel.setPlacedX(placedX);
        parcel.setPlacedY(placedY);
        parcel.setForm(form);
        return parcel;
    }
}
