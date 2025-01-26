package com.hofftech.deliverysystem.mapper;

import com.hofftech.deliverysystem.model.entity.BillingRecordEntity;
import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingRecordMapper {

    BillingRecordEntity toEntity(BillingRecord billingRecord);

    BillingRecord toDto(BillingRecordEntity billingRecordEntity);
}
