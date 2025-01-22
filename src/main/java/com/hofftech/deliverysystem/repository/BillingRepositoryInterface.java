package com.hofftech.deliverysystem.repository;

import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;

import java.time.LocalDate;
import java.util.List;

public interface BillingRepositoryInterface {

    void save(BillingRecord billing);

    List<BillingSummary> findSummaryByUserAndPeriod(String user, LocalDate from, LocalDate to);

    List<BillingRecord> findAll();
}