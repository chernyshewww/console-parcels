package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.util.FormHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParcelServiceTest {

    @Mock
    private FormHelper formHelper;

    @InjectMocks
    private ParcelService parcelService;

    private static final String FILE_PATH = "parcels.csv";

    @BeforeEach
    void setUp() throws IOException {
        // Clear the file before each test
        Files.deleteIfExists(Paths.get(FILE_PATH));
    }

    @Test
    void testCreateParcel() {
        // Arrange
        String name = "TestParcel";
        char symbol = 'T';
        char[][] form = {{'T', 'T'}, {'T', 'T'}};

        // Act
        parcelService.createParcel(name, symbol, form);

        // Assert
        assertThat(parcelService.findParcelInFile(name)).contains(name);
    }

    @Test
    void testFindParcelInFile_ParcelFound() throws IOException {
        // Arrange
        String name = "TestParcel";
        String formContent = "TT\nTT\n";
        String symbol = "T";
        String fileContent = "Name: " + name + "\nForm:\n" + formContent + "Symbol: " + symbol + "\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        // Act
        String result = parcelService.findParcelInFile(name);

        // Assert
        assertThat(result).contains(name).contains(formContent).contains(symbol);
    }

    @Test
    void testUnloadParcelsFromTrucks() {
        // Arrange
        Truck truck = new Truck(5, 5);
        Parcel parcel = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});
        truck.getParcels().add(parcel);

        // Act
        List<Parcel> parcels = parcelService.unloadParcelsFromTrucks(Collections.singletonList(truck));

        // Assert
        assertThat(parcels).hasSize(1);
        assertThat(parcels.get(0).getName()).isEqualTo("Parcel1");
    }

    @Test
    void testEditParcelInFile_ParcelFound() throws IOException {
        // Arrange
        String name = "TestParcel";
        String newName = "UpdatedParcel";
        String newForm = "UU\nUU\n";
        char newSymbol = 'U';
        String fileContent = "Name: " + name + "\nForm:\nTT\nTT\nSymbol: T\n\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        // Act
        String result = parcelService.editParcelInFile(name, newName, newForm, newSymbol);

        // Assert
        assertThat(result).contains(newName).contains(newForm).contains(String.valueOf(newSymbol));
    }

    @Test
    void testEditParcelInFile_ParcelNotFound() throws IOException {
        // Arrange
        String name = "NonExistentParcel";
        String fileContent = "Name: TestParcel\nForm:\nTT\nTT\nSymbol: T\n\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        // Act
        String result = parcelService.editParcelInFile(name, "UpdatedParcel", "UU\nUU\n", 'U');

        // Assert
        assertThat(result).contains("не найдена");
    }

    @Test
    void testDeleteParcelInFile_ParcelFound() throws IOException {
        // Arrange
        String name = "TestParcel";
        String fileContent = "Name: " + name + "\nForm:\nTT\nTT\nSymbol: T\n\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        // Act
        String result = parcelService.deleteParcelInFile(name);

        // Assert
        assertThat(result).contains("удалена");
        assertThat(Files.readAllLines(Paths.get(FILE_PATH))).isEmpty();
    }

    @Test
    void testDeleteParcelInFile_ParcelNotFound() throws IOException {
        // Arrange
        String name = "NonExistentParcel";
        String fileContent = "Name: TestParcel\nForm:\nTT\nTT\nSymbol: T\n\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        // Act
        String result = parcelService.deleteParcelInFile(name);

        // Assert
        assertThat(result).contains("не найдена");
    }

    @Test
    void testTryPlaceParcel_Success() {
        // Arrange
        Truck truck = new Truck(5, 5);
        Parcel parcel = new Parcel("Parcel1", 'P', new char[][]{{'P', 'P'}, {'P', 'P'}});

        // Act
        boolean result = parcelService.tryPlaceParcel(truck, parcel);

        // Assert
        assertThat(result).isTrue();
        assertThat(truck.getParcels()).contains(parcel);
    }
}