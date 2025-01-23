package com.hofftech.deliverysystem.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofftech.deliverysystem.config.BillingConfig;
import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;
import com.hofftech.deliverysystem.repository.impl.BillingRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingRepositoryTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private BillingConfig billingConfig;

    @InjectMocks
    private BillingRepositoryImpl billingRepository;


    @BeforeEach
    void setUp() {
        when(billingConfig.getFilePath()).thenReturn("test_billing_records.json");

        this.billingRepository.init();
    }

    @Test
    @DisplayName("Должен сохранить запись и вернуть сводку с правильными данными")
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
    @DisplayName("Должен вернуть пустой результат при отсутствии данных за указанный период")
    void testFindSummaryByUserAndPeriod_EmptyResults() {
        LocalDate from = LocalDate.now().minusDays(10);
        LocalDate to = LocalDate.now().minusDays(1);

        List<BillingSummary> summaries = billingRepository.findSummaryByUserAndPeriod("nonexistentUser", from, to);

        assertThat(summaries).isEmpty();
    }
}
