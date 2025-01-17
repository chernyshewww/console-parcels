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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // Reset mocks before each test
        reset(truckService, outputHelper, fileService);
    }

    @Test
    void testGenerateParcelOutput() {
        // Arrange
        Parcel parcel1 = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        Parcel parcel2 = new Parcel("Parcel2", 'Q', new char[][]{{'Q', 'Q'}, {'Q', 'Q'}});
        List<Parcel> parcels = Arrays.asList(parcel1, parcel2);

        // Act
        String result = outputService.generateParcelOutput(parcels);

        // Assert
        assertThat(result).contains("Посылка: Parcel1").contains("Посылка: Parcel2");
    }

    @Test
    void testGenerateLoadOutput() {
        // Arrange
        Truck truck = new Truck(5, 5);
        Parcel parcel = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        truck.getParcels().add(parcel);

        when(truckService.generateTruckView(truck)).thenReturn("PP  \nPP  \n    \n    \n    ");

        // Act
        String result = outputService.generateLoadOutput(Collections.singletonList(truck));

        // Assert
        assertThat(result).contains("Кузов грузовика:")
                .contains("Посылка: Parcel1")
                .contains("Координаты размещения:");
    }

    @Test
    void testGenerateParcelCountOutput() {
        // Arrange
        Parcel parcel1 = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        Parcel parcel2 = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        Parcel parcel3 = new Parcel("Parcel2", 'Q', new char[][]{{'Q', 'Q'}, {'Q', 'Q'}});
        List<Parcel> parcels = Arrays.asList(parcel1, parcel2, parcel3);

        // Act
        String result = outputService.generateParcelCountOutput(parcels);

        // Assert
        assertThat(result).contains("Посылка: Parcel1, Количество: 2")
                .contains("Посылка: Parcel2, Количество: 1");
    }

    @Test
    void testFormatCreateResponse() {
        // Arrange
        String name = "TestParcel";
        char[][] form = {{'T', 'T'}, {'T', 'T'}};

        // Act
        String result = outputService.formatCreateResponse(name, form);

        // Assert
        assertThat(result).contains("id(name): \"TestParcel\"")
                .contains("form:\nTT\nTT");
    }

    @Test
    void testSaveJsonOutput_Success() {
        // Arrange
        String outputFileName = "output.json";
        List<Truck> loadedTrucks = Collections.singletonList(new Truck(5, 5));
        SendMessage responseMessage = new SendMessage();

        when(outputHelper.createTruckData(any(Truck.class))).thenReturn(new HashMap<>());
        doNothing().when(fileService).saveToFile(anyString(), anyString());

        // Act
        outputService.saveJsonOutput(outputFileName, loadedTrucks, responseMessage);

        // Assert
        assertThat(responseMessage.getText()).isEqualTo("Успешно! Результат сохранен в файл: " + outputFileName);
        verify(fileService).saveToFile(eq(outputFileName), anyString());
    }

    @Test
    void testSaveJsonOutput_NoFileName() {
        // Arrange
        String outputFileName = "";
        List<Truck> loadedTrucks = Collections.singletonList(new Truck(5, 5));
        SendMessage responseMessage = new SendMessage();

        // Act
        outputService.saveJsonOutput(outputFileName, loadedTrucks, responseMessage);

        // Assert
        assertThat(responseMessage.getText()).isEqualTo("Ошибка! Для вывода в файл JSON укажите имя файла через -out-filename.");
        verify(fileService, never()).saveToFile(anyString(), anyString());
    }
}
