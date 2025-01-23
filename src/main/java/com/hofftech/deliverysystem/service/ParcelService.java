package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.record.command.LoadCommand;
import com.hofftech.deliverysystem.exception.ParcelFileReadException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.repository.impl.ParcelRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelService {

    private final ParcelRepositoryImpl parcelRepository;

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
            return parcelRepository.findAllFromCsv(commandData.parcelsFileName());
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
     * Loads parcels from the file by their names. If the parcel is found in the file,
     * it is added to the list of parcels.
     *
     * @param parcelNames A list of parcel names to search for in the file.
     * @return A list of parcels that were found in the file.
     */
    public List<Parcel> getParcelsFromFile(List<String> parcelNames) {
        List<Parcel> parcels = new ArrayList<>();
        for (String name : parcelNames) {
            Parcel parcel = parcelRepository.findByNameAsObject(name);
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

    private void placeParcel(char[][] grid, char[][] form, int row, int col, char symbol) {
        for (int i = 0; i < form.length; i++) {
            for (int j = 0; j < form[i].length; j++) {
                if (form[i][j] != '\0' && form[i][j] != ' ') {
                    grid[row + i][col + j] = symbol;
                }
            }
        }
    }
}
