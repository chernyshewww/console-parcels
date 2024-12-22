package com.deliverysystem.strategy;

import com.deliverysystem.model.Truck;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualDistributionStrategyTests {

    private EqualDistributionStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new EqualDistributionStrategy();
    }

    @Test
    void loadParcels_GivenParcels_SortedByAreaBeforeLoading() {
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'5', '5', '5', '5', '5'}
        });
        parcels.add(new char[][]{
                {'3', '3', '3'}
        });

        int availableTrucks = 2;

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(parcels.get(0).length * parcels.get(0)[0].length)
                .isGreaterThanOrEqualTo(parcels.get(0).length * parcels.get(0)[0].length);
        assertThat(parcels.get(1).length * parcels.get(1)[0].length)
                .isGreaterThanOrEqualTo(parcels.get(1).length * parcels.get(1)[0].length);
    }

    @Test
    void loadParcels_GivenAvailableTrucks_UsesOnlyAvailableTrucks() {
        List<char[][]> parcels = new ArrayList<>();
        parcels.add(new char[][]{
                {'5', '5', '5', '5', '5'}
        });
        parcels.add(new char[][]{
                {'3', '3', '3'}
        });

        int availableTrucks = 2;

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(1).getGrid()).doesNotContain(new char[]{'5', '5', '5', '5', '5'}, Index.atIndex(0));
    }

    @Test
    void loadParcels_GivenEmptyParcelList_HandlesCorrectly() {
        List<char[][]> parcels = new ArrayList<>();

        int availableTrucks = 2;

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(trucks).hasSize(2);

        for (Truck truck : trucks) {
            assertThat(truck.getGrid()).doesNotContain(new char[]{'5', '5', '5', '5', '5'}, Index.atIndex(1));
        }
    }
}
