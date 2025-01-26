package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.model.entity.TruckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckRepository extends JpaRepository<TruckEntity, String> {
}
