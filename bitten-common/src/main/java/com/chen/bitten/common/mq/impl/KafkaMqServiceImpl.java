package com.chen.bitten.common.mq.impl;

import com.chen.bitten.common.constant.MessageQueueTypeConstant;
import com.chen.bitten.common.mq.MqService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@ConditionalOnProperty(name = "bitten.mq.type", havingValue = MessageQueueTypeConstant.KAFKA)
public class KafkaMqServiceImpl implements MqService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${bitten.business.tagId.key}")
    private String TAG_ID_KEY;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (Objects.nonNull(tagId) && !tagId.isEmpty()) {
            List<Header> headers = Collections.singletonList(new RecordHeader(TAG_ID_KEY, tagId.getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(new ProducerRecord(topic, null, null, null, jsonValue, headers));
            return;
        }
        kafkaTemplate.send(topic, jsonValue);
    }

    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}
