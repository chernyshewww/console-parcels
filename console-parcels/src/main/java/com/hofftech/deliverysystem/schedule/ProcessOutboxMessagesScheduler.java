package com.hofftech.deliverysystem.schedule;

import com.hofftech.deliverysystem.service.OutboxProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessOutboxMessagesScheduler {

    private final OutboxProcessingService outboxProcessingService;

    @Scheduled(fixedDelay = 5000)
    public void processOutbox() {
        outboxProcessingService.processOutboxMessages();
    }
}