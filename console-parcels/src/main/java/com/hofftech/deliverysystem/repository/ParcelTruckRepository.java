package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.model.entity.ParcelTruckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelTruckRepository extends JpaRepository<ParcelTruckEntity, Long> {

    List<ParcelTruckEntity> findAllByTruckIdIn(List<Long> truckIds);
}
