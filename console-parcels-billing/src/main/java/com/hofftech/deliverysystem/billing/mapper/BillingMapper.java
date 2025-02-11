package com.hofftech.deliverysystem.billing.mapper;

import com.hofftech.deliverysystem.billing.model.entity.BillingEntity;
import com.hofftech.deliverysystem.billing.model.record.Billing;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingMapper {

    BillingEntity toEntity(Billing billing);

    Billing toDto(BillingEntity billingEntity);
}
