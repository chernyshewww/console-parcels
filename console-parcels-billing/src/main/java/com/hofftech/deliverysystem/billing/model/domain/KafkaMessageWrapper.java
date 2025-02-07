package com.hofftech.deliverysystem.billing.model.domain;

import java.util.Map;

public class KafkaMessageWrapper {
    private String payload;
    private Map<String, Object> headers;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }
}