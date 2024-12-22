package com.deliverysystem.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileParserServiceTests {

    @Test
    void testReadParcelsFromFile_ValidParcels() {
        String fileName = "src/test/resources/test_parcels.txt";

        List<char[][]> parcels = FileParserService.readParcelsFromFile(fileName);

        assertEquals(2, parcels.size(), "Only two valid parcels should be read from the file.");

        char[][] firstParcel = parcels.getFirst();
        char[][] expectedFirstParcel = {
                {'6', '6', '6'},
                {'6', '6', '6'}
        };
        assertArrayEquals(expectedFirstParcel, firstParcel, "The first parcel should match the expected result.");
    }

    @Test
    void testReadParcelsFromFile_InvalidParcelSkipped() {
        String fileName = "src/test/resources/test_parcels.txt";

        List<char[][]> parcels = FileParserService.readParcelsFromFile(fileName);

        assertEquals(2, parcels.size(), "Invalid parcels should be skipped.");
    }

    @Test
    void testReadParcelsFromFile_FileNotFound() {
        String invalidFileName = "src/test/resources/non_existent.txt";

        List<char[][]> parcels = FileParserService.readParcelsFromFile(invalidFileName);
        assertTrue(parcels.isEmpty(), "If the file doesn't exist, the parcel list should be empty.");
    }

    @Test
    void testReadParcelsFromFile_EmptyFile() throws Exception {
        File emptyFile = File.createTempFile("empty", ".txt");
        String fileName = emptyFile.getAbsolutePath();

        List<char[][]> parcels = FileParserService.readParcelsFromFile(fileName);

        assertTrue(parcels.isEmpty(), "An empty file should result in no parcels being read.");

        emptyFile.deleteOnExit();
    }
}