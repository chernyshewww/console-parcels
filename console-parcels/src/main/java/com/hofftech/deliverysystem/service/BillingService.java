package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.mapper.BillingRecordMapper;
import com.hofftech.deliverysystem.model.entity.BillingRecordEntity;
import com.hofftech.deliverysystem.model.record.billing.BillingRecord;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;
import com.hofftech.deliverysystem.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing billing operations, including recording load/unload transactions
 * and retrieving billing summaries for specific users and time periods.
 * <p>
 * This service interacts with the billing repository for data persistence
 * and uses the pricing service to calculate costs for various operations.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BillingService {

    private static final String LOAD = "Погрузка";
    private static final String UNLOAD = "Разгрузка";

    private final BillingRepository billingRepository;
    private final PricingService pricingService;
    private final BillingRecordMapper billingRecordMapper;


    /**
     * Records a load operation for a user, calculating the cost based on the number
     * of trucks and parcels involved.
     *
     * @param user         The email of the user performing the operation.
     * @param trucksCount  The number of trucks used for loading.
     * @param parcelsCount The number of parcels loaded.
     */
    public void recordLoadOperation(String user, int trucksCount, int parcelsCount) {
        int cost = pricingService.calculateLoadCost(trucksCount, parcelsCount);
        BillingRecord billing = new BillingRecord(user, LocalDateTime.now(), LOAD, trucksCount, parcelsCount, cost);
        BillingRecordEntity billingEntity = billingRecordMapper.toEntity(billing);

        billingRepository.save(billingEntity);
        log.info("Recorded load operation for user: {}, cost: {}", user, cost);
    }

    /**
     * Records an unload operation for a user, calculating the cost based on the number
     * of trucks and parcels involved.
     *
     * @param user         The email of the user performing the operation.
     * @param trucksCount  The number of trucks used for unloading.
     * @param parcelsCount The number of parcels unloaded.
     */
    public void recordUnloadOperation(String user, int trucksCount, int parcelsCount) {
        int cost = pricingService.calculateUnloadCost(trucksCount, parcelsCount);
        BillingRecord billing = new BillingRecord(user, LocalDateTime.now(), UNLOAD, trucksCount, parcelsCount, cost);
        BillingRecordEntity billingEntity = billingRecordMapper.toEntity(billing);
        billingRepository.save(billingEntity);
        log.info("Recorded unload operation for user: {}, cost: {}", user, cost);
    }

    /**
     * Retrieves billing summaries for a user within a specified date range.
     *
     * @param user     The email of the user whose billing data is requested.
     * @param fromDate The start date of the period.
     * @param toDate   The end date of the period.
     * @return A list of billing summaries for the specified period.
     */
    public List<BillingSummary> getBillingSummaries(String user, LocalDate fromDate, LocalDate toDate) {

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);

         return billingRepository.findSummaryByUserAndPeriod(user, fromDateTime, toDateTime).stream().map(billing -> new BillingSummary(
                billing.getTimestamp().toLocalDate(),
                billing.getOperationType(),
                billing.getSegments(),
                billing.getParcels(),
                billing.getCost()
        )).sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp())).toList();

    }
}
