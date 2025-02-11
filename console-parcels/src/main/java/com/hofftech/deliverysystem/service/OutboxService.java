package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.model.domain.OutboxMessage;
import com.hofftech.deliverysystem.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Message<String>> kafkaTemplate;

    @Transactional
    public void publishOutboxMessage(OutboxMessage message) {
        kafkaTemplate.send(
                String.format("%s-out-0", message.getMessageType()),
                "console-parcels",
                buildKafkaMessage(message)
        );
        outboxRepository.deleteById(message.getId());
    }

    private Message<String> buildKafkaMessage(OutboxMessage message) {
        return MessageBuilder
                .withPayload(message.getPayload())
                .setHeader(KafkaHeaders.KEY, message.getOwner().getBytes(StandardCharsets.UTF_8))
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
    }
}
