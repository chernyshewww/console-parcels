package com.deliverysystem.strategy;

import com.deliverysystem.model.Truck;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OneParcelPerTruckStrategyTests {

    private final OneParcelPerTruckStrategy strategy = new OneParcelPerTruckStrategy();

    @Test
    void shouldLoadOneParcelPerTruck() {
        // Given: A list of parcels and available trucks
        List<char[][]> parcels = List.of(
                new char[][] {{'1', '1'}, {'1', '1'}}, // 2x2 parcel
                new char[][] {{'2', '2'}, {'2', '2'}}  // 2x2 parcel
        );
        int availableTrucks = 3; // Three trucks available

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(trucks).hasSize(2); // Two trucks used for two parcels

        assertThat(trucks.get(0).getGrid()).contains(new char[]{'1', '1', ' ', ' ', ' ', ' '}, Index.atIndex(5));
    }

    @Test
    void shouldReturnEmptyListWhenNotEnoughTrucks() {
        // Given: A list of parcels and available trucks
        List<char[][]> parcels = List.of(
                new char[][] {{'P', 'P'}, {'P', 'P'}}, // 2x2 parcel
                new char[][] {{'Q', 'Q'}, {'Q', 'Q'}}  // 2x2 parcel
        );
        int availableTrucks = 1; // Only one truck available

        // When: We load parcels using the OneParcelPerTruckStrategy
        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        // Then: An empty list is returned because there are not enough trucks
        assertThat(trucks).isEmpty();
    }

    @Test
    void shouldHandleEmptyParcelList() {
        // Given: An empty list of parcels
        List<char[][]> parcels = List.of();

        int availableTrucks = 2; // Two trucks available

        // When: We load parcels using the OneParcelPerTruckStrategy
        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        // Then: No trucks should be used as there are no parcels
        assertThat(trucks).isEmpty();
    }
}
