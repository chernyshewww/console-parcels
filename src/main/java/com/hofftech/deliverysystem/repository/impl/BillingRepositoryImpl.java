package com.hofftech.deliverysystem.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofftech.deliverysystem.config.BillingConfig;
import com.hofftech.deliverysystem.exception.BillingException;
import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;
import com.hofftech.deliverysystem.repository.BillingRepositoryInterface;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BillingRepositoryImpl implements BillingRepositoryInterface {

    private final BillingConfig billingConfig;
    private final ObjectMapper objectMapper;
    private List<BillingRecord> billings;

    @PostConstruct
    public void init() {
        this.billings = loadBillingsFromFile();
    }

    @Override
    public void save(BillingRecord billing) {
        try {
            billings.add(billing);
            saveRecordsToFile();
            log.info("Billing record saved: {}", billing);
        } catch (BillingException e) {
            log.error("Error saving billing records to file: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<BillingSummary> findSummaryByUserAndPeriod(String user, LocalDate from, LocalDate to) {
        return billings.stream()
                .filter(billing -> billing.getUser().equals(user) &&
                        !billing.getTimestamp().toLocalDate().isBefore(from) &&
                        !billing.getTimestamp().toLocalDate().isAfter(to))
                .map(billing -> new BillingSummary(
                        billing.getTimestamp().toLocalDate(),
                        billing.getOperationType(),
                        billing.getSegments(),
                        billing.getParcels(),
                        billing.getCost()
                ))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .toList();
    }

    @Override
    public List<BillingRecord> findAll() {
        return new ArrayList<>(billings);
    }

    private List<BillingRecord> loadBillingsFromFile() {
        File file = new File(billingConfig.getFilePath());
        if (!file.exists()) {
            log.info("Billing records file not found. Starting with an empty list.");
            return new ArrayList<>();
        }

        try {
            List<BillingRecord> billingsFromFile = objectMapper.readValue(file, new TypeReference<List<BillingRecord>>() {});
            if (billingsFromFile.isEmpty()) {
                log.info("Billing records file is empty. Starting with an empty list.");
            }
            return billingsFromFile;
        } catch (IOException e) {
            log.warn("Something went wrong while reading the file. Perhaps it is empty. The empty list was created.");
            return new ArrayList<>();
        }
    }

    private void saveRecordsToFile() throws BillingException {
        try {
            objectMapper.writeValue(new File(billingConfig.getFilePath()), billings);
        } catch (IOException e) {
            throw new BillingException("Failed to save billing records to file.", e);
        }
    }
}
