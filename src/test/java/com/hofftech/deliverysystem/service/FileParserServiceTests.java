package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FileParserServiceTests {

    @Mock
    private FileReaderService fileReaderService;

    @InjectMocks
    private FileParserService fileParserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void readParcelsFromFile_GivenValidParcelsFile_ExpectedTwoParcels() {
        String fileName = "src/test/resources/test_parcels.txt";
        List<String> mockedFileLines = List.of(
                "666",
                "666",
                "",
                "666",
                "666"
        );

        when(fileReaderService.readFile(fileName)).thenReturn(mockedFileLines);

        List<Parcel> parcels = fileParserService.readParcelsFromFile(fileName);

        assertThat(parcels).hasSize(2);

        char[][] firstParcelData = parcels.get(0).getData();
        char[][] expectedFirstParcel = {
                {'6', '6', '6'},
                {'6', '6', '6'}
        };
        assertThat(firstParcelData).isDeepEqualTo(expectedFirstParcel);

        char[][] secondParcelData = parcels.get(1).getData();
        assertThat(secondParcelData).isDeepEqualTo(expectedFirstParcel);
    }

    @Test
    void readParcelsFromFile_GivenInvalidParcelFile_ExpectedInvalidParcelsSkipped() {
        String fileName = "src/test/resources/test_parcels.txt";
        List<String> mockedFileLines = List.of(
                "9999",
                "",
                "666",
                "666",
                "",
                "44444"
        );

        when(fileReaderService.readFile(fileName)).thenReturn(mockedFileLines);

        List<Parcel> parcels = fileParserService.readParcelsFromFile(fileName);

        assertThat(parcels)
                .hasSize(1)
                .extracting(Parcel::getData)
                .first()
                .isEqualTo(new char[][]{
                        {'6', '6', '6'},
                        {'6', '6', '6'}
                });
    }

    @Test
    void readParcelsFromFile_GivenNonExistentFile_ExpectedEmptyParcelList() {
        String invalidFileName = "src/test/resources/non_existent.txt";

        when(fileReaderService.readFile(invalidFileName)).thenReturn(Collections.emptyList());

        List<Parcel> parcels = fileParserService.readParcelsFromFile(invalidFileName);

        assertThat(parcels).isEmpty();
    }

    @Test
    void readParcelsFromFile_GivenEmptyFile_ExpectedNoParcelsRead() throws Exception {
        File emptyFile = File.createTempFile("empty", ".txt");
        String fileName = emptyFile.getAbsolutePath();

        when(fileReaderService.readFile(fileName)).thenReturn(Collections.emptyList());

        List<Parcel> parcels = fileParserService.readParcelsFromFile(fileName);

        assertThat(parcels).isEmpty();

        emptyFile.deleteOnExit();
    }
}
