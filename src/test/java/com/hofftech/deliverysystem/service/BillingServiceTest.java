package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;
import com.hofftech.deliverysystem.repository.BillingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BillingServiceTest {

    private BillingRepository billingRepository;
    private PricingService pricingService;
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        billingRepository = mock(BillingRepository.class);
        pricingService = mock(PricingService.class);
        billingService = new BillingService(billingRepository, pricingService);
    }

    @Test
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
        assertThat(savedRecord.getUser()).isEqualTo(user);
        assertThat(savedRecord.getOperationType()).isEqualTo("Погрузка");
        assertThat(savedRecord.getSegments()).isEqualTo(trucksCount);
        assertThat(savedRecord.getParcels()).isEqualTo(parcelsCount);
        assertThat(savedRecord.getCost()).isEqualTo(expectedCost);
        assertThat(savedRecord.getTimestamp()).isNotNull();
    }

    @Test
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
        assertThat(savedRecord.getUser()).isEqualTo(user);
        assertThat(savedRecord.getOperationType()).isEqualTo("Разгрузка");
        assertThat(savedRecord.getSegments()).isEqualTo(trucksCount);
        assertThat(savedRecord.getParcels()).isEqualTo(parcelsCount);
        assertThat(savedRecord.getCost()).isEqualTo(expectedCost);
        assertThat(savedRecord.getTimestamp()).isNotNull();
    }

    @Test
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
