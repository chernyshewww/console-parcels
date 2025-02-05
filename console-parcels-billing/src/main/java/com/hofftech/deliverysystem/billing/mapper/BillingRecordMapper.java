package com.hofftech.deliverysystem.billing.mapper;

import com.hofftech.deliverysystem.billing.model.entity.BillingRecordEntity;
import com.hofftech.deliverysystem.billing.model.record.BillingRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingRecordMapper {

    BillingRecordEntity toEntity(BillingRecord billingRecord);

    BillingRecord toDto(BillingRecordEntity billingRecordEntity);
}
