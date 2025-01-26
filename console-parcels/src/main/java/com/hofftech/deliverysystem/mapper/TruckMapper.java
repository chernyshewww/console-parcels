package com.hofftech.deliverysystem.mapper;

import com.hofftech.deliverysystem.model.Truck;
import com.hofftech.deliverysystem.model.entity.TruckEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TruckMapper {

    @Mapping(target = "grid", ignore = true)
    Truck toDto(TruckEntity entity);

    @Mapping(target = "grid", ignore = true)
    TruckEntity toEntity(Truck dto);

    @Mapping(target = "grid", ignore = true)
    List<Truck> toDto(List<TruckEntity> entities);

    @Mapping(target = "grid", ignore = true)
    List<TruckEntity> toEntity(List<Truck> dtoList);
}