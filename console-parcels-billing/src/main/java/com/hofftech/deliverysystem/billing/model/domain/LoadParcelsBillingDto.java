package com.hofftech.deliverysystem.billing.model.domain;

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
