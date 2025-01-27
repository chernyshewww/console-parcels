package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.mapper.ParcelMapper;
import com.hofftech.deliverysystem.model.entity.ParcelEntity;
import com.hofftech.deliverysystem.model.record.command.CreateCommand;
import com.hofftech.deliverysystem.model.record.command.EditCommand;
import com.hofftech.deliverysystem.model.record.command.LoadCommand;
import com.hofftech.deliverysystem.exception.ParcelFileReadException;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.repository.ParcelRepository;
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

    private final ParcelRepository parcelRepository;
    private final ParcelMapper parcelMapper;
    private final ParcelTruckService parcelTruckService;

    /**
     * Loads parcels from the file based on the provided command data.
     * The data can either be a list of parcel names or a filename.
     *
     * @param commandData The command data containing the parcel names or filename.
     * @return A list of parcels.
     * @throws ParcelFileReadException If an error occurs while reading the file.
     */
    public List<Parcel> loadParcels(LoadCommand commandData) {
        var entities = new ArrayList<ParcelEntity>();
        if (!commandData.parcels().equals("All")) {
            List<String> parcelNames = Arrays.asList(commandData.parcels().split("\\\\n"));
            entities.addAll(parcelRepository.findAllByNameIn(parcelNames));
        } else {
            entities.addAll(parcelRepository.findAll());
        }

        var result = new ArrayList<Parcel>();
        for (ParcelEntity parcel : entities) {
            result.add(createParcel(parcel.getName(), Arrays.stream(parcel.getForm().split("\\\\n")).toList(), parcel.getSymbol()));
        }

        return result;
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

    public Parcel findByName(String name) {
        return parcelMapper.toDto(parcelRepository.findByName(name));
    }

    public Parcel create(CreateCommand commandData) {
        ParcelEntity parcelEntity = new ParcelEntity();
        parcelEntity.setForm(commandData.form());
        parcelEntity.setName(commandData.name());
        parcelEntity.setSymbol(commandData.symbol());

        return parcelMapper.toDto(parcelRepository.save(parcelEntity));
    }

    public void delete(String name) {
        parcelRepository.deleteByName(name);
    }

    public Parcel edit(EditCommand commandData) {
        ParcelEntity existingParcel = parcelRepository.findByName(commandData.id());

        if (existingParcel == null) {
            return null;
        }

        existingParcel.setName(commandData.newName());
        existingParcel.setSymbol(commandData.newSymbol());
        existingParcel.setForm(commandData.newForm());

        return parcelMapper.toDto(parcelRepository.save(existingParcel));
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

    public List<Parcel> findParcelsByTruckIds(List<Long> truckIds) {
        List<String> parcelNames = parcelTruckService.findNamesByTruckIds(truckIds);

        return parcelMapper.toDto(parcelRepository.findAllByNameIn(parcelNames));
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
