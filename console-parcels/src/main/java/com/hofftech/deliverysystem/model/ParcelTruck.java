package com.hofftech.deliverysystem.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParcelTruck {

    private Long id;

    private Long truckId;

    private String parcelName;

    private String coordinates;
}
