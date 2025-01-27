package com.chen.bitten.data.receiver.kafka;

import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.constant.MessageQueueTypeConstant;
import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.data.service.ConsumeService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("logReceiver")
@ConditionalOnProperty(name = "bitten.mq.type", havingValue = MessageQueueTypeConstant.KAFKA)
public class Receiver {

    @Autowired
    @Qualifier("logConsumeService")
    private ConsumeService consumeService;

    @KafkaListener(topics = "#{'${bitten.business.log.topic.name}'}", groupId = "#{'${bitten.business.log.groupId}'}")
    public void logConsumer(ConsumerRecord<?, String> consumerRecord) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            AnchorInfo anchorInfo = JSON.parseObject(kafkaMessage.get(), AnchorInfo.class);
            consumeService.consumeLog(anchorInfo);
        }
    }


}
