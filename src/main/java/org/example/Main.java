package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static final int TRUCK_WIDTH = 6;
    public static final int TRUCK_HEIGHT = 6;

    // PROGRAM WILL SKIP ALL THE INVALID SEQUENCES AND WRITE WARNING ABOUT IT
    public static final List<String> VALID_SEQUENCES = Arrays.asList(
            "1",
            "22",
            "333",
            "4444",
            "55555",
            "666\n666",
            "777\n7777",
            "8888\n8888",
            "999\n999\n999"
    );

    // The word 'package' is reserved in java, so we use 'parcel' instead
    public static void main(String[] args) {
        logger.info("Program started.");

        // Read parcels from file
        var parcels = readParcelsFromFile("src/main/resources/parcels.txt");
        logger.info("Read {} valid parcels from file.", parcels.size());

        // Load parcels into trucks with first strategy
        logger.info("Using Strategy 1 (Largest First).");
        var trucksStrategy1 = loadParcelsIntoTrucks(parcels, 1);

        System.out.println("\nStrategy 1: Largest First");
        printTrucks(trucksStrategy1);

        // Load parcels into trucks with second strategy
        // Very strange strategy, but still it works
        logger.info("Using Strategy 2 (One-to-one)");
        var trucksStrategy2 = loadParcelsIntoTrucks(parcels, 2);

        System.out.println("\nStrategy 2: One-to-one");
        printTrucks(trucksStrategy2);

        logger.info("Program finished. Total trucks used: Strategy 1: " + trucksStrategy1.size() + ", Strategy 2: " + trucksStrategy2.size());
    }

    public static List<char[][]> readParcelsFromFile(String fileName) {
        List<char[][]> parcels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<String> currentParcel = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!currentParcel.isEmpty()) {
                        if (isValidParcel(currentParcel)) {
                            parcels.add(convertToMatrix(currentParcel));
                        } else {
                            logger.warn("Invalid parcel skipped: " + currentParcel);
                        }
                        currentParcel.clear();
                    }
                } else {
                    currentParcel.add(line);
                }
            }

            if (!currentParcel.isEmpty()) {
                if (isValidParcel(currentParcel)) {
                    parcels.add(convertToMatrix(currentParcel));
                } else {
                    logger.warn("Invalid parcel skipped: " + currentParcel);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading file: " + e.getMessage());
        }
        return parcels;
    }

    public static boolean isValidParcel(List<String> parcelLines) {
        var parcelBuilder = new StringBuilder();

        for (String line : parcelLines) {
            parcelBuilder.append(line).append("\n");
        }

        var parcelString = parcelBuilder.toString().trim();
        return VALID_SEQUENCES.contains(parcelString);
    }

    public static char[][] convertToMatrix(List<String> lines) {
        var height = lines.size();
        var width = lines.get(0).length();
        char[][] matrix = new char[height][width];
        for (var i = 0; i < height; i++) {
            matrix[i] = lines.get(i).toCharArray();
        }
        return matrix;
    }
    public static boolean containsChar(char[][] array, char target) {
        for (char[] row : array) {
            for (char cell : row) {
                if (cell == target) {
                    return true;
                }
            }
        }
        return false;
    }
    public static List<char[][]> loadParcelsIntoTrucks(List<char[][]> parcels, int strategy) {
        List<char[][]> trucks = new ArrayList<>();

        parcels.sort((a, b) -> Boolean.compare(!containsChar(a, '7'), !containsChar(b, '7')));

        if (strategy == 1) {
            // Strategy 1: We will sort the parcels by size
            parcels.sort((a, b) -> Integer.compare(b.length * b[0].length, a.length * a[0].length));
            logger.info("Parcels sorted by size (largest first)");
        } else {
            logger.info("Using Strategy 2: One parcel - One truck");
        }

        for (char[][] parcel : parcels) {
            logger.info("Placing parcel of size " + parcel.length + "x" + parcel[0].length);

            if (strategy == 2) {
                // Strategy 2: One parcel per truck
                char[][] newTruck = new char[TRUCK_HEIGHT][TRUCK_WIDTH];
                for (char[] row : newTruck) {
                    Arrays.fill(row, ' ');
                }
                placeParcel(parcel, newTruck, TRUCK_HEIGHT - parcel.length, 0); // Place parcel at bottom left
                trucks.add(newTruck);
                logger.info("New truck created for parcel");
            } else {
                // Strategy 1: Fit parcels into existing trucks
                boolean placed = false;
                for (char[][] truck : trucks) {
                    if (placeParcelInTruck(parcel, truck)) {
                        logger.info("Parcel successfully placed in existing truck");
                        placed = true;
                        break;
                    }
                }

                if (!placed) {
                    logger.info("Parcel does not fit in existing trucks. Creating new truck");
                    char[][] newTruck = new char[TRUCK_HEIGHT][TRUCK_WIDTH];
                    for (char[] row : newTruck) {
                        Arrays.fill(row, ' ');
                    }
                    placeParcel(parcel, newTruck, TRUCK_HEIGHT - parcel.length, 0); // Place parcel at bottom left
                    trucks.add(newTruck);
                }
            }
        }
        return trucks;
    }

    public static boolean placeParcelInTruck(char[][] parcel, char[][] truck) {
        var parcelHeight = parcel.length;
        var parcelWidth = parcel[0].length;

        for (var row = TRUCK_HEIGHT - parcelHeight; row >= 0; row--) {
            for (var col = 0; col <= TRUCK_WIDTH - parcelWidth; col++) {
                if (canPlaceParcel(parcel, truck, row, col)) {
                    placeParcel(parcel, truck, row, col);
                    logger.info("Parcel placed at position: row " + row + ", col " + col);
                    return true;
                }
            }
        }
        logger.warn("Failed to place parcel of size " + parcelHeight + "x" + parcelWidth);
        return false;
    }

    // I hate this algorithm
    public static boolean canPlaceParcel(char[][] parcel, char[][] truck, int row, int col) {
        var parcelHeight = parcel.length;
        var parcelWidth = parcel[0].length;

        if (row + parcelHeight > TRUCK_HEIGHT || col + parcelWidth > TRUCK_WIDTH) {
            return false;
        }

        for (var i = 0; i < parcelHeight; i++) {
            for (var j = 0; j < parcelWidth; j++) {
                if (parcel[i][j] != ' ' && truck[row + i][col + j] != ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    public static void placeParcel(char[][] parcel, char[][] truck, int row, int col) {
        var parcelHeight = parcel.length;

        for (var i = 0; i < parcelHeight; i++) {
            var parcelWidth = parcel[i].length;
            for (var j = 0; j < parcelWidth; j++) {
                if (parcel[i][j] != ' ') {
                    truck[row + i][col + j] = parcel[i][j];
                }
            }
        }
    }

    public static void printTrucks(List<char[][]> trucks) {
        for (var t = 0; t < trucks.size(); t++) {
            System.out.println("Truck " + (t + 1) + ":");
            printTruck(trucks.get(t));
            System.out.println();
        }
    }

    public static void printTruck(char[][] truck) {
        System.out.println("+      +");

        for (char[] row : truck) {
            System.out.print("+");
            for (char cell : row) {
                System.out.print(cell == ' ' ? ' ' : cell);
            }
            System.out.println("+");
        }

        System.out.println("++++++++");
    }
}