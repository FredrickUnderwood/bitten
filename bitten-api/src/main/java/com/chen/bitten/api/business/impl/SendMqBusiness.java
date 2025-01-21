package com.chen.bitten.api.business.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chen.bitten.common.domain.ResponseTaskInfo;
import com.chen.bitten.common.domain.SendTaskModel;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.mq.MqService;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SendMqBusiness implements BusinessProcess<SendTaskModel> {

    private static final String LOG_PREFIX = "[SendMqBusiness]";

    @Autowired
    private MqService mqService;

    @Value("${bitten.business.topic.name}")
    private String topic;

    @Value("${bitten.business.tagId.value}")
    private String tagIdValue;

    @Value("${bitten.mq.type}")
    private String mqType;


    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        List<TaskInfo> taskInfoList = sendTaskModel.getTaskInfoList();
        try {
            String message = JSON.toJSONString(taskInfoList, SerializerFeature.WriteClassName);
            mqService.send(topic, message, tagIdValue);
            context.setResponse(BasicResultVO.success(taskInfoList.stream().
                    map(taskInfo -> ResponseTaskInfo.builder().businessId(taskInfo.getBusinessId())
                            .bizId(taskInfo.getBizId()).messageId(taskInfo.getMessageId()).build()).collect(Collectors.toList())));
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVER_ERROR));
            log.error("{}Send {} fail! param: {} e: {}", LOG_PREFIX, mqType,
                    JSON.toJSONString(CollUtil.getFirst(taskInfoList.listIterator())),e.getStackTrace());
        }
    }
}
