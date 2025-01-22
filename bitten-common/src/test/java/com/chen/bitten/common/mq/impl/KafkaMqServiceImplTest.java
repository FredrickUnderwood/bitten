package com.chen.bitten.common.mq.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class KafkaMqServiceImplTest {

    @InjectMocks
    private KafkaMqServiceImpl kafkaMqService;

    @Mock
    private KafkaTemplate kafkaTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void send() {
        ReflectionTestUtils.setField(kafkaMqService, "TAG_ID_KEY", "kafka_tag_id");
        String topic = "bittenBusiness";
        String tagId = "com.chen.bitten";
        kafkaMqService.send(topic, "[\n" +
                "  {\n" +
                "    \"receiver\": [\n" +
                "      \"3314333502@qq.com\",\n" +
                "      \"fredrickunderwood2002@gmail.com\"\n" +
                "    ],\n" +
                "    \"sendChannel\": 10,\n" +
                "    \"messageType\": 10,\n" +
                "    \"contentModel\": {\n" +
                "      \"@type\": \"com.chen.bitten.common.dto.model.EmailContentModel\",\n" +
                "      \"title\": \"test-title\",\n" +
                "      \"content\": \"test-email-content\"\n" +
                "    },\n" +
                "    \n" +
                "    \"sendAccount\": 1,\n" +
                "    \"shieldType\": 10,\n" +
                "    \"messageTemplateId\": 11\n" +
                "  }\n" +
                "]" , tagId);
    }

    @Test
    void testSend() {
    }
}