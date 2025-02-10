package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.domain.OutboxMessage;
import com.hofftech.deliverysystem.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxProcessingService {

    private final OutboxRepository outboxRepository;
    private final OutboxService outboxService;

    public void processOutboxMessages() {
        List<OutboxMessage> messages = outboxRepository.findAll();
        for (OutboxMessage message : messages) {
            outboxService.publishOutboxMessage(message);
        }
    }
}
