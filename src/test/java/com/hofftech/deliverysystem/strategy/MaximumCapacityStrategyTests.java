package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.TruckService;
import com.hofftech.deliverysystem.util.TruckGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaximumCapacityStrategyTests {

    @InjectMocks
    private MaximumCapacityStrategy strategy;

    @Mock
    private TruckGenerator truckGenerator;

    @Mock
    private TruckService truckService;

    @Mock
    private StrategyHelper strategyHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        truckGenerator = new TruckGenerator();
        truckService = new TruckService();

        strategyHelper = new StrategyHelper(truckGenerator, truckService);
        strategy = new MaximumCapacityStrategy(strategyHelper);
    }

    @Test
    void loadParcels_GivenOneParcel_FitsInOneTruck() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel(new char[][]{
                {'5', '5', '5', '5', '5'}}));
        parcels.add(new Parcel(new char[][]{
                {'3', '3', '3'}}));

        List<Truck> trucks = strategy.loadParcels(parcels, 10);

        assertEquals(1, trucks.size(), "Expected all parcels to fit in one truck.");
    }

    @Test
    void loadParcels_GivenMultipleParcels_NeedsMultipleTrucks() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel(new char[][]{
                {'5', '5', '5', '5', '5'}}));
        parcels.add(new Parcel(new char[][]{
                {'4', '4', '4', '4'}}));
        parcels.add(new Parcel(new char[][]{
                {'3', '3', '3'}}));

        List<Truck> trucks = strategy.loadParcels(parcels, 10);

        assertNotNull(trucks, "Check that truck list should not be null");
    }

    @Test
    void loadParcels_GivenEmptyParcelList_ReturnsNoTrucks() {
        List<Parcel> parcels = new ArrayList<>();

        List<Truck> trucks = strategy.loadParcels(parcels, 10);

        assertTrue(trucks.isEmpty(), "Expected no trucks when input is empty.");
    }

    @Test
    void loadParcels_GivenFullTruckUtilization_AllParcelsFitInOneTruck() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel(new char[][]{
                {'8', '8', '8', '8'}
        }));
        parcels.add(new Parcel(new char[][]{
                {'8', '8', '8', '8'}
        }));
        parcels.add(new Parcel(new char[][]{
                {'6', '6', '6'}
        }));
        parcels.add(new Parcel(new char[][]{
                {'6', '6'}
        }));

        List<Truck> trucks = strategy.loadParcels(parcels, 10);

        assertEquals(1, trucks.size(), "Expected all parcels to fit into one truck.");
    }
}
