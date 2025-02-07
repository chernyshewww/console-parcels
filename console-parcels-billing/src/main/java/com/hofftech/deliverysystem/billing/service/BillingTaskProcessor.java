package com.hofftech.deliverysystem.billing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "loadParcelsBilling-out-0", groupId = "delivery-system-group", containerFactory = "kafkaListenerContainerFactory")
    public void processLoad(KafkaMessageWrapper task) {
        try {
            LoadParcelsBillingDto payload = objectMapper.readValue(task.getPayload(), LoadParcelsBillingDto.class);

            billingService.recordLoadOperation(payload);
        } catch (JsonProcessingException e) {
            log.error("Error parsing payload: {}", task.getPayload(), e);
        } catch (Exception e) {
            log.error("Error processing billing  loadtask: {}", task, e);
        }
    }

    @KafkaListener(topics = "unloadParcelsBilling-out-0", groupId = "delivery-system-group", containerFactory = "kafkaListenerContainerFactory")
    public void processUnLoad(KafkaMessageWrapper task) {
        log.info("Received unloading task: {}", task);
        try {
            LoadParcelsBillingDto payload = objectMapper.readValue(task.getPayload(), LoadParcelsBillingDto.class);

            billingService.recordUnloadOperation(payload);
        } catch (JsonProcessingException e) {
            log.error("Error parsing payload: {}", task.getPayload(), e);
        }
        catch (Exception e) {
            log.error("Error parsing or processing billing unload task: {}", task, e);
        }
    }
}
