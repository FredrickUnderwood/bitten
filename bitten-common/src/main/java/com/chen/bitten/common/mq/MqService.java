package com.chen.bitten.common.mq;

public interface MqService {

    void send(String topic, String jsonValue, String tagId);

    void send(String topic, String jsonValue);
}
