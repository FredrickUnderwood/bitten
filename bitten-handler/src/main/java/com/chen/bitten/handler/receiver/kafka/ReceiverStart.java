package com.chen.bitten.handler.receiver.kafka;

import cn.hutool.core.text.StrPool;
import com.chen.bitten.common.constant.MessageQueueTypeConstant;
import com.chen.bitten.handler.utils.GroupIdUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 启动消费者
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "bitten.mq.type", havingValue = MessageQueueTypeConstant.KAFKA)
public class ReceiverStart {
    /**
     * 用来匹配需要启动的receiver的方法名
     */
    private static final String RECEIVER_METHOD_NAME = "Receiver.consumer";
    /**
     * 初始化所有的groupIds
     */
    private static final List<String> GROUP_IDS = GroupIdUtils.getAllGroupIds();

    private static Integer index = 0;
    @Autowired
    private ApplicationContext context;


    /**
     * 给每个Receiver对象的consumer方法 @KafkaListener赋值相应的groupId
     */
    @Bean
    public static KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer groupIdEnhancer() {
        return (attrs, element) -> {
            if (element instanceof Method) {
                String methodName = ((Method) element).getDeclaringClass().getSimpleName() + StrPool.DOT + ((Method) element).getName();
                if (methodName.equals(RECEIVER_METHOD_NAME)) {
                    attrs.put("groupId", GROUP_IDS.get(index++));
                }
            }
            return attrs;
        };

    }

    /**
     * 为每个渠道的消息创建一个Receiver
     */
    @PostConstruct
    public void init() {
        for (int i = 0; i < GROUP_IDS.size(); i++) {
            context.getBean(Receiver.class);
        }
    }
}
