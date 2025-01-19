package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.util.OutputHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OutputServiceTest {

    @Mock
    private TruckService truckService;

    @Mock
    private OutputHelper outputHelper;

    @Mock
    private FileService fileService;

    @InjectMocks
    private OutputService outputService;

    @BeforeEach
    void setUp() {
        reset(truckService, outputHelper, fileService);
    }

    @Test
    void testGenerateParcelOutput() {
        Parcel parcel1 = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        Parcel parcel2 = new Parcel("Parcel2", 'Q', new char[][]{{'Q', 'Q'}, {'Q', 'Q'}});
        List<Parcel> parcels = Arrays.asList(parcel1, parcel2);

        String result = outputService.generateParcelOutput(parcels);

        assertThat(result).contains("Посылка: Parcel1").contains("Посылка: Parcel2");
    }

    @Test
    void testGenerateLoadOutput() {
        Truck truck = new Truck(5, 5);
        Parcel parcel = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        truck.getParcels().add(parcel);

        when(truckService.generateTruckView(truck)).thenReturn("PP  \nPP  \n    \n    \n    ");

        String result = outputService.generateLoadOutput(Collections.singletonList(truck));

        assertThat(result).contains("Кузов грузовика:")
                .contains("Посылка: Parcel1")
                .contains("Координаты размещения:");
    }

    @Test
    void testGenerateParcelCountOutput() {
        Parcel parcel1 = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        Parcel parcel2 = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        Parcel parcel3 = new Parcel("Parcel2", 'Q', new char[][]{{'Q', 'Q'}, {'Q', 'Q'}});
        List<Parcel> parcels = Arrays.asList(parcel1, parcel2, parcel3);

        String result = outputService.generateParcelCountOutput(parcels);

        assertThat(result).contains("Посылка: Parcel1, Количество: 2")
                .contains("Посылка: Parcel2, Количество: 1");
    }

    @Test
    void testFormatCreateResponse() {
        String name = "TestParcel";
        char[][] form = {{'T', 'T'}, {'T', 'T'}};

        String result = outputService.formatCreateResponse(name, form);

        assertThat(result).contains("id(name): \"TestParcel\"")
                .contains("form:\nTT\nTT");
    }
}
