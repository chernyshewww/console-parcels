package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.record.command.LoadCommand;
import com.hofftech.deliverysystem.exception.ParcelFileException;
import com.hofftech.deliverysystem.exception.ParcelFileReadException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelService {

    private final FormHelper formHelper;

    private final List<Parcel> parcelList = new ArrayList<>();
    private static final String FILE_PATH = "parcels.csv";
    private static final String NAME = "Name:";
    private static final String FORM = "Form:";
    private static final String SYMBOL = "Symbol:";
    private static final String PARCEL_WITH_NAME = "Посылка с именем \"";
    private static final String NOT_FOUND = "\" не найдена.";

    /**
     * Creates a new parcel and adds it to the list of parcels.
     * Writes the parcel information to a file.
     *
     * @param name The name of the parcel.
     * @param symbol The symbol representing the parcel.
     * @param form The form of the parcel represented as a 2D array.
     */
    public void createParcel(String name, char symbol, char[][] form) {
        Parcel parcel = new Parcel(name, symbol, form);
        parcelList.add(parcel);
        log.info("Создана посылка: {}", parcel.getName());

        writeParcelToFile(parcel);
    }

    /**
     * Finds a parcel in the file by name and returns its details.
     *
     * @param name The name of the parcel to find.
     * @return A string representing the parcel details or an error message if not found.
     */
    public String findParcelInFile(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            String parcelName = null;
            StringBuilder formString = new StringBuilder();
            boolean isFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(NAME)) {
                    if (parcelName != null && parcelName.equals(name)) {
                        isFound = true;
                    }

                    parcelName = processName(line);
                }

                else if (line.startsWith(FORM)) {
                    formString.setLength(0);
                    processForm(reader, formString);
                }
            }

            if (parcelName != null && parcelName.equals(name)) {
                isFound = true;
            }

            return buildResponse(isFound, name, formString.toString());

        } catch (IOException e) {
            return "Ошибка при чтении файла: " + e.getMessage();
        }
    }

    /**
     * Loads parcels from the file based on the provided command data.
     * The data can either be a list of parcel names or a filename.
     *
     * @param commandData The command data containing the parcel names or filename.
     * @return A list of parcels.
     * @throws ParcelFileReadException If an error occurs while reading the file.
     */
    public List<Parcel> loadParcels(LoadCommand commandData) {
        if (commandData.parcelsText() != null) {
            List<String> parcelNames = Arrays.asList(commandData.parcelsText().split("\\\\n"));
            return getParcelsFromFile(parcelNames);
        } else {
            return getParcelsFromCsv(commandData.parcelsFileName());
        }
    }

    /**
     * Unloads parcels from a list of trucks and returns them as a list.
     *
     * @param trucks The list of trucks from which to unload parcels.
     * @return A list of parcels unloaded from the trucks.
     */
    public List<Parcel> unloadParcelsFromTrucks(List<Truck> trucks) {
        List<Parcel> parcels = new ArrayList<>();
        for (Truck truck : trucks) {
            parcels.addAll(truck.getParcels());
        }
        return parcels;
    }

    /**
     * Finds a parcel in the file by its name and returns the parcel object if found.
     *
     * @param name The name of the parcel to find.
     * @return The found Parcel object or null if not found.
     */
    public Parcel findParcelInFileByName(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            String currentName = null;
            List<String> form = new ArrayList<>();
            Character symbol = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(NAME)) {
                    if (currentName != null && currentName.equals(name)) {
                        return createParcel(currentName, form, symbol);
                    }

                    currentName = line.substring(5).trim();
                    form.clear();
                    symbol = null;
                } else if (line.startsWith(FORM)) {
                    form.clear();
                } else if (line.startsWith(SYMBOL)) {
                    symbol = line.charAt(8);
                } else if (!line.isEmpty() && currentName != null) {
                    form.add(line);
                }
            }

            if (currentName != null && currentName.equals(name)) {
                return createParcel(currentName, form, symbol);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Edits the details of a parcel in the file (name, form, symbol) based on the given ID.
     *
     * @param id The ID (name) of the parcel to edit.
     * @param newName The new name for the parcel.
     * @param newForm The new form of the parcel.
     * @param newSymbol The new symbol for the parcel.
     * @return A string indicating the result of the update (success or error).
     */
    public String editParcelInFile(String id, String newName, String newForm, char newSymbol) {
        try {

            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            StringBuilder updatedContent = new StringBuilder();

            boolean isParcelFound = false;
            boolean isFormSection = false;
            boolean isSymbolSection = false;

            for (String line : lines) {
                if (line.startsWith(NAME) && line.contains(id)) {
                    isParcelFound = true;
                    updatedContent.append(NAME + " ").append(newName).append("\n");
                    isFormSection = true;
                    continue;
                }

                if (isParcelFound && isFormSection) {
                    if (line.startsWith(FORM)) {
                        updatedContent.append(FORM + "\n");
                        String updatedForm = newForm.replace("x", String.valueOf(newSymbol));
                        updatedContent.append(updatedForm.replace("\\n", "\n")).append("\n");
                        isFormSection = false;
                        isSymbolSection = true;
                        continue;
                    }
                }

                if (isParcelFound && isSymbolSection) {
                    if (line.startsWith(SYMBOL)) {
                        updatedContent.append(SYMBOL + " ").append(newSymbol).append("\n");
                        isSymbolSection = false;
                    }
                    continue;
                }

                updatedContent.append(line).append("\n");
            }

            if (isParcelFound) {
                Files.write(path, updatedContent.toString().getBytes());

                String formOutput = newForm.replace("x", String.valueOf(newSymbol)).replace("\\n", "\n");
                return NAME + " " + newName + "\nForm:\n" + formOutput + "\nSymbol: " + newSymbol;
            } else {
                return PARCEL_WITH_NAME + id + NOT_FOUND;
            }

        } catch (IOException e) {
            return "Ошибка при обновлении файла: " + e.getMessage();
        }
    }

    /**
     * Deletes a parcel from the file based on the parcel's name.
     *
     * @param parcelName The name of the parcel to delete.
     * @return A string indicating whether the parcel was deleted successfully or not found.
     */
    public String deleteParcelInFile(String parcelName) {
        try {
            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>();

            boolean isParcelFound = false;
            boolean isDeleting = false;

            for (String line : lines) {
                if (line.startsWith(NAME) && line.contains(parcelName)) {
                    isParcelFound = true;
                    isDeleting = true;
                    continue;
                }

                if (isDeleting) {
                    if (line.startsWith(FORM) || line.startsWith(SYMBOL)) {
                        continue;
                    }

                    if (line.startsWith(NAME)) {
                        isDeleting = false;
                    }
                }

                if (!isParcelFound || !isDeleting) {
                    updatedLines.add(line);
                }
            }

            Files.write(path, updatedLines);

            if (isParcelFound) {
                return "\"" + parcelName + "\" удалена.";
            } else {
                return PARCEL_WITH_NAME + parcelName + NOT_FOUND;
            }

        } catch (IOException e) {
            throw new ParcelFileException("Ошибка при удалении посылки: " + e.getMessage(), e);
        }
    }

    /**
     * Reads parcels from a CSV file and returns them as a list of Parcel objects.
     * Each parcel is created with a name, form (as a matrix), and a symbol.
     *
     * @param fileName The name of the CSV file to read the parcels from.
     * @return A list of parcels loaded from the CSV file.
     * @throws ParcelFileReadException If an error occurs while reading the file.
     */
    public List<Parcel> getParcelsFromCsv(String fileName) throws ParcelFileReadException {
        List<Parcel> parcels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String parcelName = null;
            List<int[]> formLines = new ArrayList<>();
            char symbol = ' ';

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(NAME)) {
                    parcelName = line.substring(5).trim();
                } else if (line.startsWith(FORM)) {
                    formLines.clear();
                } else if (line.startsWith(SYMBOL)) {
                    symbol = line.substring(7).trim().charAt(0);
                } else if (!line.isEmpty()) {
                    formLines.add(line.chars().map(c -> c == ' ' ? 0 : 1).toArray());
                } else if (parcelName != null && !formLines.isEmpty()) {
                    char[][] form = toMatrix(formLines);
                    Parcel parcel = new Parcel(parcelName, form);
                    parcel.setSymbol(symbol);
                    parcels.add(parcel);

                    parcelName = null;
                    formLines.clear();
                    symbol = ' ';
                }
            }

            if (parcelName != null && !formLines.isEmpty()) {
                char[][] form = toMatrix(formLines);
                Parcel parcel = new Parcel(parcelName, form);
                parcel.setSymbol(symbol);
                parcels.add(parcel);
            }
        } catch (IOException e) {
            throw new ParcelFileReadException("Ошибка при чтении CSV-файла: " + fileName, e);
        }
        return parcels;
    }

    /**
     * Loads parcels from the file by their names. If the parcel is found in the file,
     * it is added to the list of parcels.
     *
     * @param parcelNames A list of parcel names to search for in the file.
     * @return A list of parcels that were found in the file.
     */
    public List<Parcel> getParcelsFromFile(List<String> parcelNames) {
        List<Parcel> parcels = new ArrayList<>();
        for (String name : parcelNames) {
            Parcel parcel = findParcelInFileByName(name);
            if (parcel != null) {
                parcels.add(parcel);
            }
        }
        return parcels;
    }

    /**
     * Attempts to place a parcel on a truck grid. It tries to fit the parcel's form onto the grid.
     * If successful, the parcel is placed at the given position, and its coordinates are recorded.
     *
     * @param truck The truck where the parcel should be placed.
     * @param parcel The parcel that is being placed on the truck.
     * @return true if the parcel was successfully placed, false otherwise.
     */
    public boolean tryPlaceParcel(Truck truck, Parcel parcel) {
        char[][] grid = truck.getGrid();
        char[][] form = parcel.getForm();

        for (int row = grid.length - form.length; row >= 0; row--) {
            for (int col = 0; col <= grid[0].length - form[0].length; col++) {
                if (canPlace(grid, form, row, col)) {
                    placeParcel(grid, form, row, col, parcel.getSymbol());

                    parcel.setPlacedX(row);
                    parcel.setPlacedY(col);
                    parcel.setPlaced(true);

                    truck.getParcels().add(parcel);

                    return true;
                }
            }
        }
        return false;
    }

    private boolean canPlace(char[][] grid, char[][] form, int row, int col) {
        for (int i = 0; i < form.length; i++) {
            for (int j = 0; j < form[i].length; j++) {

                if (form[i][j] != '\0' && form[i][j] != ' ' && grid[row + i][col + j] != '\0' && grid[row + i][col + j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private Parcel createParcel(String name, List<String> form, Character symbol) {
        Parcel parcel = new Parcel();
        parcel.setName(name);


        char[][] formMatrix = new char[form.size()][];
        for (int i = 0; i < form.size(); i++) {
            formMatrix[i] = form.get(i).toCharArray();
        }
        parcel.setForm(formMatrix);

        parcel.setSymbol(symbol);
        return parcel;
    }

    private void placeParcel(char[][] grid, char[][] form, int row, int col, char symbol) {
        for (int i = 0; i < form.length; i++) {
            for (int j = 0; j < form[i].length; j++) {
                if (form[i][j] != '\0' && form[i][j] != ' ') {
                    grid[row + i][col + j] = symbol;
                }
            }
        }
    }

    private void writeParcelToFile(Parcel parcel) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            String formString = formHelper.getFormAsString(parcel.getForm());
            writer.write("Name: " + parcel.getName() + "\n");
            writer.write("Form:\n" + formString);
            writer.write("Symbol: " + parcel.getSymbol() + "\n");
            writer.write("\n");
        } catch (IOException e) {
            log.error("Ошибка при записи в файл: {}", e.getMessage());
        }
    }

    private char[][] toMatrix(List<int[]> formLines) {

        char[][] matrix = new char[formLines.size()][];

        for (int i = 0; i < formLines.size(); i++) {
            int[] intRow = formLines.get(i);
            char[] charRow = new char[intRow.length];

            for (int j = 0; j < intRow.length; j++) {
                charRow[j] = (char) (intRow[j] + '0');
            }

            matrix[i] = charRow;
        }
        return matrix;
    }

    private String processName(String line) {
        return line.substring(5).trim();
    }

    private void processForm(BufferedReader reader, StringBuilder formString) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !line.startsWith(SYMBOL)) {
            formString.append(line).append("\n");
        }
    }

    private String buildResponse(boolean isFound, String name, String formContent) {
        if (isFound) {
            return NAME + name + "\nForm:\n" + formContent.trim() + "\n";
        } else {
            return PARCEL_WITH_NAME + name + NOT_FOUND;
        }
    }
}
