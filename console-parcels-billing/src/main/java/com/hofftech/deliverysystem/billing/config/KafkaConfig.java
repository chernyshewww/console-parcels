package com.hofftech.deliverysystem.billing.config;

import com.hofftech.deliverysystem.billing.model.domain.KafkaMessageWrapper;
import com.hofftech.deliverysystem.billing.model.domain.LoadParcelsBillingDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, KafkaMessageWrapper> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new ErrorHandlingDeserializer<>(new StringDeserializer()), // Handle key deserialization errors
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(KafkaMessageWrapper.class)) // Handle value deserialization errors
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,KafkaMessageWrapper> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessageWrapper> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3); // Adjust concurrency based on your needs
        return factory;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "delivery-system-group"); // Set a group ID
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start from the earliest offset
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // Trust all packages for deserialization
        props.put(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, true); // Remove type headers
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); // Do not use type headers
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); // Enable auto-commit
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false); // Disable auto topic creation
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        return props;
    }
}