package com.chen.bitten.handler.config;

import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Configuration
public class KafkaConfig {


    @Autowired
    private ConsumerFactory consumerFactory;
    /**
     * 针对tag消息过滤
     * producer 将tag写进header里
     *
     * @return true 消息将会被丢弃
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory(@Value("${bitten.business.tagId.key}") String tagIdKey,
                                                                          @Value("${bitten.business.tagId.value}") String tagIdValue) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setAckDiscarded(true);
        /*
        过滤策略
         */
        factory.setRecordFilterStrategy(consumerRecord -> {
            if (Optional.ofNullable(consumerRecord.value()).isPresent()) {
                for (Header header: consumerRecord.headers()) {
                    if (header.key().equals(tagIdKey) &&
                            new String(header.value(), StandardCharsets.UTF_8).equals(tagIdValue)) {
                        return false;
                    }
                }
            }
            return true;
        });
        return factory;
    }
}
