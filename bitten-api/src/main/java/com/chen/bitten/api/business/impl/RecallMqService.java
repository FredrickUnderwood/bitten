package com.chen.bitten.api.business.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chen.bitten.common.domain.RecallTaskModel;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.mq.MqService;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecallMqService implements BusinessProcess<RecallTaskModel> {

    public static final String LOG_PREFIX = "[RecallMqService]";

    @Autowired
    private MqService mqService;

    @Value("${bitten.business.recall.topic.name}")
    private String topic;

    @Value("${bitten.business.tagId.value}")
    private String tagValue;

    @Value("${bitten.mq.type}")
    private String mqType;

    @Override
    public void process(ProcessContext<RecallTaskModel> context) {
        RecallTaskModel recallTaskModel = context.getProcessModel();
        try {
            String message = JSON.toJSONString(recallTaskModel, SerializerFeature.WriteClassName);
            mqService.send(topic, message, tagValue);
            context.setResponse(BasicResultVO.success());
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVER_ERROR));
            log.error("{}send {} fail! e: {}", LOG_PREFIX, mqType,e.getStackTrace());
        }
    }
}
