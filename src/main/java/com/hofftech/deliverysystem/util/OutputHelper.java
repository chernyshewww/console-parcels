package com.hofftech.deliverysystem.util;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OutputHelper {

    /**
     * Creates a map representation of truck data, including truck type and parcels.
     *
     * @param truck The truck whose data is to be created.
     * @return A map containing truck type and a list of parcels data.
     */
    public Map<String, Object> createTruckData(Truck truck) {
        Map<String, Object> truckData = new HashMap<>();
        truckData.put("truck_type", truck.getWidth() + "x" + truck.getHeight());

        List<Map<String, Object>> parcelsData = createParcelsData(truck);
        truckData.put("parcels", parcelsData);

        return truckData;
    }

    /**
     * Creates a list of parcel data for the given truck.
     *
     * @param truck The truck whose parcels' data is to be created.
     * @return A list of maps, each representing data for a parcel in the truck.
     */
    private List<Map<String, Object>> createParcelsData(Truck truck) {
        List<Map<String, Object>> parcelsData = new ArrayList<>();

        for (Parcel parcel : truck.getParcels()) {
            Map<String, Object> parcelData = createParcelData(parcel);
            parcelsData.add(parcelData);
        }

        return parcelsData;
    }

    /**
     * Creates a map representation of parcel data.
     *
     * @param parcel The parcel whose data is to be created.
     * @return A map containing parcel name and coordinates of placed positions.
     */
    private Map<String, Object> createParcelData(Parcel parcel) {
        Map<String, Object> parcelData = new HashMap<>();
        parcelData.put("name", parcel.getName());

        List<List<Integer>> coordinates = getParcelCoordinates(parcel);
        parcelData.put("coordinates", coordinates);

        return parcelData;
    }

    /**
     * Retrieves the coordinates of the parcel in the truck grid where the parcel is placed.
     *
     * @param parcel The parcel whose coordinates are to be retrieved.
     * @return A list of lists containing coordinates where the parcel's '1' cells are located.
     */
    private List<List<Integer>> getParcelCoordinates(Parcel parcel) {
        List<List<Integer>> coordinates = new ArrayList<>();

        for (int row = 0; row < parcel.getForm().length; row++) {
            for (int col = 0; col < parcel.getForm()[row].length; col++) {
                if (parcel.getForm()[row][col] == '1') {
                    coordinates.add(Arrays.asList(parcel.getPlacedX() + row, parcel.getPlacedY() + col));
                }
            }
        }

        return coordinates;
    }
}
