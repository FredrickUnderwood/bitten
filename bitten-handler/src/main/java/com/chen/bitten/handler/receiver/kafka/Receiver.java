package com.chen.bitten.handler.receiver.kafka;

import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.handler.utils.GroupIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 消费MQ的消息
 */
@Slf4j
@Component
public class Receiver {
    /**
     * 发送消息consumer
     *
     * @param consumerRecord 接收到的消息记录
     * @param topicGroupId 通过@Header注解获取Kafka消息头的GroupId
     */
    @KafkaListener(topics = "#{'${bitten.business.topic.name}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            // 判断Kafka消息是否为空
            List<TaskInfo> taskInfoList = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            String messageGroupId = GroupIdUtils.getGroupIdByTaskInfo(Objects.requireNonNull(taskInfoList.stream().findFirst().orElse(null)));
            // 只有跟自身相关的消息才消费
            if (messageGroupId.equals(topicGroupId)) {
                // TODO 执行发送逻辑
            }

        }
    }

    //TODO 撤回消息consumer
}
