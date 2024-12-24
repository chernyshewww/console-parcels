//package com.hofftech.deliverysystem.strategy;
//
//import com.hofftech.deliverysystem.model.Truck;
//import org.assertj.core.data.Index;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//@Disabled
//class OneParcelPerTruckStrategyTests {
//
//    private final OneParcelPerTruckStrategy strategy = new OneParcelPerTruckStrategy();
//
//    @Test
//    void loadParcels_GivenTwoParcelsAndThreeTrucks_LoadsOneParcelPerTruck() {
//        List<char[][]> parcels = List.of(
//                new char[][] {{'1', '1'}, {'1', '1'}},
//                new char[][] {{'2', '2'}, {'2', '2'}}
//        );
//        int availableTrucks = 3;
//
//        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);
//
//        assertThat(trucks).hasSize(2);
//
//        assertThat(trucks.getFirst().getGrid()).contains(new char[]{'1', '1', ' ', ' ', ' ', ' '}, Index.atIndex(5));
//    }
//
//    @Test
//    void loadParcels_GivenTwoParcelsAndOneTruck_ReturnsEmptyList() {
//        List<char[][]> parcels = List.of(
//                new char[][] {{'P', 'P'}, {'P', 'P'}},
//                new char[][] {{'Q', 'Q'}, {'Q', 'Q'}}
//        );
//        int availableTrucks = 1;
//
//        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);
//
//        assertThat(trucks).isEmpty();
//    }
//
//    @Test
//    void loadParcels_GivenEmptyParcelList_ReturnsEmptyList() {
//        List<char[][]> parcels = List.of();
//
//        int availableTrucks = 2;
//
//        List<Truck> trucks = strategy.loadParcels(parcels, availableTrucks);
//
//        assertThat(trucks).isEmpty();
//    }
//}
