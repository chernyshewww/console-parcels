package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.mapper.TruckMapper;
import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.model.entity.ParcelTruckEntity;
import com.hofftech.deliverysystem.model.entity.TruckEntity;
import com.hofftech.deliverysystem.repository.ParcelTruckRepository;
import com.hofftech.deliverysystem.repository.TruckRepository;
import com.hofftech.deliverysystem.util.TruckGridHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * Service class for handling truck-related operations such as generating truck views,
 * parsing truck sizes from a text, and loading trucks from a file.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TruckService {

    private final TruckRepository truckrepository;
    private final TruckMapper truckMapper;
    private final TruckGridHelper truckGridHelper;
    private final ParcelTruckService parcelTruckService;

    /**
     * Generates a string representation of a truck's grid, where each cell in the grid is
     * represented by a space if it is empty or the corresponding character for filled cells.
     *
     * @param truck The truck whose grid view is to be generated.
     * @return A string representing the truck's grid.
     */
    public String generateTruckView(Truck truck) {
        StringBuilder view = new StringBuilder();
        char[][] grid = truck.getGrid();

        for (char[] row : grid) {
            for (char cell : row) {
                view.append(cell == '\0' ? ' ' : cell);
            }
            view.append("\n");
        }
        return view.toString();
    }

    /**
     * Parses a string containing truck dimensions (in the format "width x height")
     * and converts them into a list of Truck objects.
     *
     * Each truck's width and height are separated by 'x', and each truck's dimensions are
     * separated by a new line.
     *
     * @param trucksText The string containing truck dimensions.
     * @return A list of Truck objects created based on the parsed dimensions.
     */
    public List<Truck> parseTruckSizes(String trucksText) {
        List<Truck> trucks = new ArrayList<>();
        String[] truckDimensions = trucksText.split("\\\\n");

        for (String dimension : truckDimensions) {
            String[] dimensions = dimension.split("x");
            if (dimensions.length == 2) {
                try {
                    int width = Integer.parseInt(dimensions[0].trim());
                    int height = Integer.parseInt(dimensions[1].trim());
                    trucks.add(new Truck(width, height));
                } catch (NumberFormatException e) {
                    log.error("Ошибка при разборе размера грузовика: {}", dimension);
                }
            }
        }

        return trucks;
    }

    public void saveTrucks(List<Truck> loadedTrucks){
        for(Truck truck : loadedTrucks) {
            TruckEntity truckEntity = new TruckEntity();
            truckEntity.setHeight(truck.getHeight());
            truckEntity.setWidth(truck.getWidth());
            truckEntity.setGrid(truckGridHelper.parseGridToString(truck.getGrid()));
            truckrepository.save(truckEntity);

            if (!truck.getParcels().isEmpty()) {
                for(Parcel parcel : truck.getParcels()) {
                    parcelTruckService.save(parcel, truckEntity.getId());
                }
            }
        }
    }

    public List<Long> findAllIds() {
        return truckrepository.findAll().stream().map(TruckEntity::getId).toList();
    }
}
