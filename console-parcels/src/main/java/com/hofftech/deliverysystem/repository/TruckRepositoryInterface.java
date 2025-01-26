package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.exception.TruckFileReadException;
import com.hofftech.deliverysystem.model.Truck;

import java.util.List;

public interface TruckRepositoryInterface {

    List<Truck> loadFromFile(String inputFileName) throws TruckFileReadException;
}
