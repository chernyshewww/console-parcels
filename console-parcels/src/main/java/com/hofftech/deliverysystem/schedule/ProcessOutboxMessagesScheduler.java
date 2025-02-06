package com.hofftech.deliverysystem.schedule;

import com.hofftech.deliverysystem.model.domain.OutboxMessage;
import com.hofftech.deliverysystem.repository.OutboxRepository;
import com.hofftech.deliverysystem.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessOutboxMessagesScheduler {

    private final OutboxRepository outboxRepository;
    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        List<OutboxMessage> messages = outboxRepository.findAll();
        for (OutboxMessage message : messages) {
            outboxService.publishOutboxMessage(message);
        }
    }
}
