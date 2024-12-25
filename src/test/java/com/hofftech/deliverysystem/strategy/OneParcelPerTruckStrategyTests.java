package com.hofftech.deliverysystem.strategy;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.service.TruckService;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OneParcelPerTruckStrategyTests {

    @InjectMocks
    private OneParcelPerTruckStrategy strategy;

    @Mock
    private TruckService truckService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        truckService = new TruckService();
        strategy = new OneParcelPerTruckStrategy(truckService);
    }

    @Test
    void loadParcels_GivenTwoParcelsAndThreeTrucks_LoadsOneParcelPerTruck() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel(new char[][]{
                {'1', '1'}, {'1', '1'}}));
        parcels.add(new Parcel(new char[][]{
                {'2', '2'}, {'2', '2'}}));
        int availableTrucks = 3;

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(trucks).hasSize(2);

        assertThat(trucks.getFirst().getGrid()).contains(new char[]{'1', '1', ' ', ' ', ' ', ' '}, Index.atIndex(5));
    }

    @Test
    void loadParcels_GivenTwoParcelsAndOneTruck_ReturnsEmptyList() {
        List<Parcel> parcels = new ArrayList<>();
        parcels.add(new Parcel(new char[][]{
                {'P', 'P'}, {'Q', 'Q'}}));
        parcels.add(new Parcel(new char[][]{
                {'Q', 'Q'}, {'Q', 'Q'}}));
        int availableTrucks = 1;

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(trucks).isEmpty();
    }

    @Test
    void loadParcels_GivenEmptyParcelList_ReturnsEmptyList() {
        List<Parcel> parcels = List.of();

        int availableTrucks = 2;

        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);

        assertThat(trucks).isEmpty();
    }
}
