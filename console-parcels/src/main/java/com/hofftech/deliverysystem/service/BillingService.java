package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.exception.BillingSendException;
import com.hofftech.deliverysystem.model.LoadParcelsBillingDto;
import com.hofftech.deliverysystem.model.domain.OutboxMessage;
import com.hofftech.deliverysystem.model.record.billing.BillingSummary;
import com.hofftech.deliverysystem.repository.BillingRepository;
import com.hofftech.deliverysystem.repository.OutboxRepository;
import com.hofftech.deliverysystem.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final OutboxRepository outboxRepository;
    private final BillingRepository billingRepository;

    public void addLoadParcelsBilling(String user, int parcelsCount, int trucksCount) {
        if (user == null || user.isEmpty()) {
            throw new BillingSendException("Необходимо указать пользователя");
        }

        var message = createOutboxMessage(user, parcelsCount, trucksCount, "loadParcelsBilling");
        outboxRepository.save(message);
    }


    public void addUnloadParcelsBilling(String user, int parcelscount, int trucksCount) {
        if (user == null || user.isEmpty()) {
            throw new BillingSendException("Необходимо указать пользователя");
        }

        var message = createOutboxMessage(user, parcelscount, trucksCount, "unloadParcelsBilling");
        outboxRepository.save(message);
    }

    private OutboxMessage createOutboxMessage(String user, int parcelsCount, int trucksCount, String messageType) {
        var message = new OutboxMessage();
        message.setMessageType(messageType);
        message.setPayload(JsonUtil.toJson(new LoadParcelsBillingDto(
                UUID.randomUUID(),
                user,
                parcelsCount,
                trucksCount
        )));
        message.setCreatedAt(LocalDateTime.now());
        message.setOwner(user);
        return message;
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

