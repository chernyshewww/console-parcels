package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.Parcel;
import com.hofftech.deliverysystem.model.entity.ParcelTruckEntity;
import com.hofftech.deliverysystem.repository.ParcelTruckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelTruckService {

    private final ParcelTruckRepository parcelTruckRepository;

    public List<String> findNamesByTruckIds(List<Long> truckIds) {
        return parcelTruckRepository.findAllByTruckIdIn(truckIds).stream().map(ParcelTruckEntity::getParcelName).toList();
    }

    public void save(Parcel parcel, Long truckId) {
        ParcelTruckEntity parcelTruckEntity = new ParcelTruckEntity();
        parcelTruckEntity.setParcelName(parcel.getName());
        parcelTruckEntity.setTruckId(truckId);
        parcelTruckEntity.setCoordinates(parcel.getPlacedCoordinates());
        parcelTruckRepository.save(parcelTruckEntity);
    }
}
