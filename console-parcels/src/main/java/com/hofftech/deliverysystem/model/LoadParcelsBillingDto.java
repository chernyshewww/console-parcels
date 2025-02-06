package com.hofftech.deliverysystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class LoadParcelsBillingDto {
    private UUID messageId;
    private String user;
    private int trucksCount;
    private int parcelsCount;
}
