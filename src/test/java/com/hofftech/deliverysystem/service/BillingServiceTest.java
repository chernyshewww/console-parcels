package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;
import com.hofftech.deliverysystem.repository.impl.BillingRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @Mock
    private BillingRepositoryImpl billingRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private BillingService billingService;

    @Test
    @DisplayName("Должен сохранить запись о операции погрузки при вызове метода")
    void recordLoadOperation_ShouldSaveBillingRecord_WhenCalled() {
        String user = "test@example.com";
        int trucksCount = 2;
        int parcelsCount = 10;
        int expectedCost = 500;

        when(pricingService.calculateLoadCost(trucksCount, parcelsCount)).thenReturn(expectedCost);

        billingService.recordLoadOperation(user, trucksCount, parcelsCount);

        ArgumentCaptor<BillingRecord> captor = ArgumentCaptor.forClass(BillingRecord.class);
        verify(billingRepository).save(captor.capture());

        BillingRecord savedRecord = captor.getValue();
        assertThat(savedRecord)
                .extracting("user", "operationType", "segments", "parcels", "cost", "timestamp")
                .containsExactly(user, "Погрузка", trucksCount, parcelsCount, expectedCost, savedRecord.getTimestamp());
    }

    @Test
    @DisplayName("Должен сохранить запись о операции разгрузки при вызове метода")
    void recordUnloadOperation_ShouldSaveBillingRecord_WhenCalled() {
        String user = "test@example.com";
        int trucksCount = 3;
        int parcelsCount = 15;
        int expectedCost = 750;

        when(pricingService.calculateUnloadCost(trucksCount, parcelsCount)).thenReturn(expectedCost);

        billingService.recordUnloadOperation(user, trucksCount, parcelsCount);

        ArgumentCaptor<BillingRecord> captor = ArgumentCaptor.forClass(BillingRecord.class);
        verify(billingRepository).save(captor.capture());

        BillingRecord savedRecord = captor.getValue();
        assertThat(savedRecord)
                .extracting("user", "operationType", "segments", "parcels", "cost", "timestamp")
                .containsExactly(user, "Разгрузка", trucksCount, parcelsCount, expectedCost, savedRecord.getTimestamp());
    }

    @Test
    @DisplayName("Должен вернуть сводки по операциям, если они есть")
    void getBillingSummaries_ShouldReturnBillingSummaries_WhenCalled() {
        String user = "test@example.com";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 1, 31);
        List<BillingSummary> expectedSummaries = List.of(
                new BillingSummary(LocalDate.of(2023, 1, 15), "Погрузка", 2, 10, 500),
                new BillingSummary(LocalDate.of(2023, 1, 20), "Разгрузка", 3, 15, 750)
        );

        when(billingRepository.findSummaryByUserAndPeriod(user, fromDate, toDate)).thenReturn(expectedSummaries);

        List<BillingSummary> result = billingService.getBillingSummaries(user, fromDate, toDate);

        assertThat(result).containsExactlyElementsOf(expectedSummaries);
    }
}
