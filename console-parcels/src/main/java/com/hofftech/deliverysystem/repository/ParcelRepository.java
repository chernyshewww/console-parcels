package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.model.entity.ParcelEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelRepository extends JpaRepository<ParcelEntity, String> {

    @Query("SELECT p FROM ParcelEntity p WHERE p.name = :name ")
    ParcelEntity findByName(String name);

    List<ParcelEntity> findAllByNameIn(List<String> names);

    @Transactional
    @Modifying
    @Query("DELETE FROM ParcelEntity p WHERE p.name = :name")
    void deleteByName(@Param("name") String name);
}
