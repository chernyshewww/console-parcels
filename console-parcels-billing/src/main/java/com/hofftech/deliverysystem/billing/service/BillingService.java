package com.hofftech.deliverysystem.billing.service;

import com.hofftech.deliverysystem.billing.mapper.BillingRecordMapper;
import com.hofftech.deliverysystem.billing.model.InboxMessage;
import com.hofftech.deliverysystem.billing.model.domain.LoadParcelsBillingDto;
import com.hofftech.deliverysystem.billing.model.entity.BillingRecordEntity;
import com.hofftech.deliverysystem.billing.model.record.BillingRecord;
import com.hofftech.deliverysystem.billing.model.record.BillingSummary;
import com.hofftech.deliverysystem.billing.repository.BillingRepository;
import com.hofftech.deliverysystem.billing.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


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
    private final InboxRepository inboxRepository;
    private final PricingService pricingService;
    private final BillingRecordMapper billingRecordMapper;

    @Transactional
    @CacheEvict(value = "billing", key = "#message.user")
    public void recordLoadOperation(LoadParcelsBillingDto message) {
        if (inboxRepository.findById(message.getMessageId()).isPresent()) {
            return;
        }

        int cost = pricingService.calculateLoadCost(message.getTrucksCount(), message.getParcelsCount());
        BillingRecord billing = new BillingRecord(message.getUser(), LocalDateTime.now(), LOAD,
                message.getTrucksCount(),
                message.getParcelsCount(), cost);
        BillingRecordEntity billingEntity = billingRecordMapper.toEntity(billing);

        billingRepository.save(billingEntity);
        addInboxMessage(message.getMessageId(), message.getUser());
        log.info("Recorded load operation for user: {}, cost: {}", message.getUser(), cost);
    }

    @Transactional
    @CacheEvict(value = "billing", key = "#message.user")
    public void recordUnloadOperation(LoadParcelsBillingDto message) {
        if (inboxRepository.findById(message.getMessageId()).isPresent()) {
            return;
        }

        int cost = pricingService.calculateUnloadCost(message.getTrucksCount(), message.getParcelsCount());
        BillingRecord billing = new BillingRecord(message.getUser(), LocalDateTime.now(), UNLOAD,
                message.getTrucksCount(),
                message.getParcelsCount(), cost);
        BillingRecordEntity billingEntity = billingRecordMapper.toEntity(billing);
        billingRepository.save(billingEntity);
        addInboxMessage(message.getMessageId(), message.getUser());
        log.info("Recorded unload operation for user: {}, cost: {}", message.getUser(), cost);
    }

    public List<BillingSummary> findBillings(String user, LocalDate fromDate, LocalDate toDate) {
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

    /**
     * Retrieves billing summaries for a user within a specified date range.
     *
     * @param user     The email of the user whose billing data is requested.
     * @param fromDate The start date of the period.
     * @param toDate   The end date of the period.
     * @return A list of billing summaries for the specified period.
     */
    @Cacheable(value = "billing", key = "#user")
    public List<BillingSummary> findBillingSummariesByLastMonth(String user, LocalDate fromDate, LocalDate toDate) {

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

    private void addInboxMessage(UUID messageId, String user) {
        var message = new InboxMessage();
        message.setId(messageId);
        message.setOwner(user);
        message.setCreatedAt(LocalDateTime.now());
        inboxRepository.save(message);
    }
}
