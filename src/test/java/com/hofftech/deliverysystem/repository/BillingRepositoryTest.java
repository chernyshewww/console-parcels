package com.hofftech.deliverysystem.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BillingRepositoryTest {

    @InjectMocks
    private BillingRepository billingRepository;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        billingRepository = new BillingRepository(objectMapper);

        File file = new File("billing_records.json");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveBillingRecord(){
        BillingRecord billingRecord = new BillingRecord(
                "user123",
                LocalDateTime.now(),
                "LOAD",
                3,
                10,
                100
        );

        billingRepository.save(billingRecord);

        List<BillingSummary> summaries = billingRepository.findSummaryByUserAndPeriod(
                "user123", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertThat(summaries).hasSize(1);

        BillingSummary summary = summaries.get(0);
        assertThat(summary.getOperationType()).isEqualTo("LOAD");
        assertThat(summary.getSegments()).isEqualTo(3);
        assertThat(summary.getParcels()).isEqualTo(10);
        assertThat(summary.getCost()).isEqualTo(100);
    }

    @Test
    void testFindSummaryByUserAndPeriod_EmptyResults() {
        LocalDate from = LocalDate.now().minusDays(10);
        LocalDate to = LocalDate.now().minusDays(1);

        List<BillingSummary> summaries = billingRepository.findSummaryByUserAndPeriod("nonexistentUser", from, to);

        assertThat(summaries).isEmpty();
    }
}
