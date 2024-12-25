package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.util.TruckGenerator;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualDistributionStrategyTests {

    @InjectMocks
    private EqualDistributionStrategy strategy;

    @Mock
    private StrategyHelper strategyHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TruckGenerator truckGenerator = new TruckGenerator();
        strategy = new EqualDistributionStrategy(strategyHelper, truckGenerator);
    }

    @Test
    void loadParcels_GivenAvailableTrucks_UsesOnlyAvailableTrucks() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel(new char[][]{
                {'5', '5', '5', '5', '5'}}));
        parcels.add(new Parcel(new char[][]{
                {'3', '3', '3'}}));

        int availableTrucks = 2;

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(1).getGrid()).doesNotContain(new char[]{'5', '5', '5', '5', '5'}, Index.atIndex(0));
    }

    @Test
    void loadParcels_GivenEmptyParcelList_HandlesCorrectly() {
        List<Parcel> parcels = new ArrayList<>();

        int availableTrucks = 2;

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(trucks).hasSize(2);

        for (Truck truck : trucks) {
            assertThat(truck.getGrid()).doesNotContain(new char[]{'5', '5', '5', '5', '5'}, Index.atIndex(1));
        }
    }
}
