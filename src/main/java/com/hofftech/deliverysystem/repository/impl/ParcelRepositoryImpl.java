package com.hofftech.deliverysystem.repository.impl;

import com.hofftech.deliverysystem.exception.ParcelFileException;
import com.hofftech.deliverysystem.exception.ParcelFileReadException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.repository.ParcelRepositoryInterface;
import com.hofftech.deliverysystem.util.FormHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ParcelRepositoryImpl implements ParcelRepositoryInterface {

    private static final String FILE_PATH = "parcels.csv";
    private static final String NAME = "Name:";
    private static final String FORM = "Form:";
    private static final String SYMBOL = "Symbol:";
    private static final String PARCEL_WITH_NAME = "Посылка с именем \"";
    private static final String NOT_FOUND = "\" не найдена.";

    private final FormHelper formHelper;

    @Override
    public String findByName(String name) {
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

    @Override
    public Parcel findByNameAsObject(String name) {
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

    @Override
    public String update(String id, String newName, String newForm, char newSymbol) {
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

    @Override
    public String deleteByName(String parcelName) throws ParcelFileException {
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

    @Override
    public List<Parcel> findAllFromCsv(String fileName) throws ParcelFileReadException {
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

    @Override
    public void create(String name, char symbol, char[][] form) {
        Parcel parcel = new Parcel(name, symbol, form);
        log.info("Создана посылка: {}", parcel.getName());

        writeParcelToFile(parcel);
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

    private String processName(String line) {
        return line.substring(5).trim();
    }

    private void processForm(BufferedReader reader, StringBuilder formString) throws IOException {
        String line;
        while ((line = reader.readLine()) != null && !line.startsWith(SYMBOL)) {
            formString.append(line).append("\n");
        }
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

    private String buildResponse(boolean isFound, String name, String formContent) {
        if (isFound) {
            return NAME + name + "\nForm:\n" + formContent.trim() + "\n";
        } else {
            return PARCEL_WITH_NAME + name + NOT_FOUND;
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
}
