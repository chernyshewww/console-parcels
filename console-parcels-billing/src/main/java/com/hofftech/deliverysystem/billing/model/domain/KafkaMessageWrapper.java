package com.hofftech.deliverysystem.billing.model.domain;

import java.util.Map;

public class KafkaMessageWrapper<T> {
    private T payload;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "KafkaMessageWrapper{" +
                "payload=" + payload +
                '}';
    }
}