package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.repository.impl.ParcelRepositoryImpl;
import com.hofftech.deliverysystem.util.FormHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ParcelRepositoryTest {

    @InjectMocks
    private ParcelRepositoryImpl parcelRepository;

    @Mock
    private FormHelper formHelper;

    private static final String FILE_PATH = "parcels.csv";

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_PATH));
    }

    @Test
    @DisplayName("Должен создать посылку и найти её по имени")
    void testCreateParcel() {
        String name = "TestParcel";
        char symbol = 'T';
        char[][] form = {{'T', 'T'}, {'T', 'T'}};

        parcelRepository.create(name, symbol, form);

        assertThat(parcelRepository.findByName(name)).contains(name);
    }

    @Test
    @DisplayName("Должен найти посылку в файле, если она существует")
    void testFindParcelInFile_ParcelFound() throws IOException {
        String name = "TestParcel";
        String formContent = "TT\nTT\n";
        String symbol = "T";
        String fileContent = "Name: " + name + "\nForm:\n" + formContent + "Symbol: " + symbol + "\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        String result = parcelRepository.findByName(name);

        assertThat(result).contains(name).contains(formContent).contains(symbol);
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если посылка не найдена в файле")
    void testFindParcelInFile_ParcelNotFound() throws IOException {
        String name = "NonExistentParcel";
        String fileContent = "Name: TestParcel\nForm:\nTT\nTT\nSymbol: T\n\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        String result = parcelRepository.findByName(name);

        assertThat(result).contains("не найдена");
    }

    @Test
    @DisplayName("Должен обновить посылку в файле, если она найдена")
    void testEditParcelInFile_ParcelFound() throws IOException {
        String name = "TestParcel";
        String newName = "UpdatedParcel";
        String newForm = "UU\nUU\n";
        char newSymbol = 'U';
        String fileContent = "Name: " + name + "\nForm:\nTT\nTT\nSymbol: T\n\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        String result = parcelRepository.update(name, newName, newForm, newSymbol);

        assertThat(result).contains(newName).contains(newForm).contains(String.valueOf(newSymbol));
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если посылка не найдена при попытке обновить")
    void testEditParcelInFile_ParcelNotFound() throws IOException {
        String name = "NonExistentParcel";
        String fileContent = "Name: TestParcel\nForm:\nTT\nTT\nSymbol: T\n\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        String result = parcelRepository.update(name, "UpdatedParcel", "UU\nUU\n", 'U');

        assertThat(result).contains("не найдена");
    }

    @Test
    @DisplayName("Должен удалить посылку в файле, если она найдена")
    void testDeleteParcelInFile_ParcelFound() throws IOException {
        String name = "TestParcel";
        String fileContent = "Name: " + name + "\nForm:\nTT\nTT\nSymbol: T\n\n";
        Path path = Paths.get(FILE_PATH);
        Files.write(path, fileContent.getBytes());

        String result = parcelRepository.deleteByName(name);

        assertThat(result).contains("удалена");
        assertThat(Files.readAllLines(path)).isEmpty();
    }

    @Test
    @DisplayName("Должен вернуть ошибку, если посылка не найдена при попытке удалить")
    void testDeleteParcelInFile_ParcelNotFound() throws IOException {
        String name = "NonExistentParcel";
        String fileContent = "Name: TestParcel\nForm:\nTT\nTT\nSymbol: T\n\n";
        Files.write(Paths.get(FILE_PATH), fileContent.getBytes());

        String result = parcelRepository.deleteByName(name);

        assertThat(result).contains("не найдена");
    }
}
