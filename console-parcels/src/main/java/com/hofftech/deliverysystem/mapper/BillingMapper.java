package com.hofftech.deliverysystem.mapper;

import com.hofftech.deliverysystem.model.entity.BillingEntity;
import com.hofftech.deliverysystem.model.record.billing.Billing;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingMapper {

    BillingEntity toEntity(Billing billing);

    Billing toDto(BillingEntity billingEntity);
}
