package com.hofftech.deliverysystem.mapper;

import com.hofftech.deliverysystem.model.ParcelTruck;
import com.hofftech.deliverysystem.model.entity.ParcelTruckEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParcelTruckMapper {

    ParcelTruck toDto(ParcelTruckEntity entity);

    ParcelTruckEntity toEntity(ParcelTruck dto);

    List<ParcelTruck> toDto(List<ParcelTruckEntity> entities);

    List<ParcelTruckEntity> toEntity(List<ParcelTruck> dtoList);

}
