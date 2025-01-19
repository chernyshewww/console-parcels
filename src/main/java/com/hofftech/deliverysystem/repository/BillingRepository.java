package com.hofftech.deliverysystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BillingRepository {

    private static final String FILE_PATH = "billing_records.json";
    private final ObjectMapper objectMapper;
    private final List<BillingRecord> billings;

    public BillingRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.billings = loadBillingsFromFile();
    }

    private List<BillingRecord> loadBillingsFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            log.info("Billing records file not found. Starting with an empty list.");
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<BillingRecord>>() {});
        } catch (IOException e) {
            log.error("Error reading billing records from file: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void save(BillingRecord billing) {
        billings.add(billing);
        saveRecordsToFile();
        log.info("Billing record saved: {}", billing);
    }

    private void saveRecordsToFile() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), billings);
        } catch (IOException e) {
            log.error("Error saving billing records to file: {}", e.getMessage(), e);
        }
    }

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
}
