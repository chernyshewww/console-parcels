package com.hofftech.deliverysystem.billing.service;

import com.hofftech.deliverysystem.billing.model.domain.KafkaMessageWrapper;
import com.hofftech.deliverysystem.billing.model.domain.LoadParcelsBillingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingTaskProcessor {

    private final BillingService billingService;

    @KafkaListener(topics = "loadParcelsBilling-out-0", groupId = "delivery-system-group", containerFactory = "kafkaListenerContainerFactory")
    public void processLoad(KafkaMessageWrapper<LoadParcelsBillingDto> task) {
        log.info("Received loading task: {}", task);

        try{
            billingService.recordLoadOperation(task.getPayload());
        } catch (Exception e) {
            log.error("Error processing billing  loadtask: {}", task, e);
        }
    }

    @KafkaListener(topics = "unloadParcelsBilling-out-0", groupId = "delivery-system-group", containerFactory = "kafkaListenerContainerFactory")
    public void processUnLoad(KafkaMessageWrapper<LoadParcelsBillingDto> task) {
        log.info("Received unloading task: {}", task);

        try {
            billingService.recordUnloadOperation(task.getPayload());
        }
        catch (Exception e) {
            log.error("Error parsing or processing billing unload task: {}", task, e);
        }
    }
}
