package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.exception.ParcelFileException;
import com.hofftech.deliverysystem.exception.ParcelFileReadException;

import java.util.List;

public interface ParcelRepositoryInterface {

    String findByName(String name);

    Parcel findByNameAsObject(String name);

    String update(String id, String newName, String newForm, char newSymbol);

    String deleteByName(String parcelName) throws ParcelFileException;

    List<Parcel> findAllFromCsv(String fileName) throws ParcelFileReadException;

    void create(String name, char symbol, char[][] form);
}
