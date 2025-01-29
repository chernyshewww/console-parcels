package com.hofftech.deliverysystem.telegram.config;

import com.hofftech.deliverysystem.telegram.client.DeliverySystemRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class DeliverySystemRestConfig {

    @Value("${services.url.delivery-system-rest-client}")
    private String deliverySystemRestUrl;

    @Bean
    public DeliverySystemRestClient deliverySystemRestClient() {

        var restClient = RestClient.builder().baseUrl(deliverySystemRestUrl).build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory =
                HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxyFactory.createClient(DeliverySystemRestClient.class);
    }
}