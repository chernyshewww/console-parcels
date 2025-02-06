package com.hofftech.deliverysystem.billing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingTaskProcessor {

    private final BillingService billingService;

    @KafkaListener(topics = "billing-load-topic", groupId = "delivery-system-group", containerFactory = "kafkaListenerContainerFactory")
    public void processLoad(@Payload String task) {
        log.info("Received sorting task: {}", task);
        billingService.recordLoadOperation(task, 1, 2);
    }
//
//    @KafkaListener(topics = "billing-unload-topic", groupId = "delivery-system-group" containerFactory = "kafkaListenerContainerFactory")
//    public void processUnload(@Payload String task) {
//    }
}
