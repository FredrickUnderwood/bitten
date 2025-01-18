package com.chen.bitten.handler.service.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.AnchorStateEnum;
import com.chen.bitten.common.enums.ShieldTypeEnum;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.utils.LogUtils;
import com.chen.bitten.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShieldBusiness implements BusinessProcess<TaskInfo> {

    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_REDIS_KEY = "night_shield_but_next_day_send";
    private static final Long SECONDS_OF_A_DAY = 86400L;
    /**
     * 默认8点之前是凌晨
     */
    private static final Integer TIME_THRESHOLD = 8;

    @Autowired
    private LogUtils logUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void process(ProcessContext<TaskInfo> context) {
        TaskInfo taskInfo = context.getProcessModel();
        if (taskInfo.getShieldType().equals(ShieldTypeEnum.NIGHT_NO_SHIELD.getCode())) {
            return;
        }
        if (LocalDateTime.now().getHour() < TIME_THRESHOLD) {
            if (taskInfo.getShieldType().equals(ShieldTypeEnum.NIGHT_SHIELD.getCode())) {
                logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId())
                        .bizId(taskInfo.getBizId()).messageId(taskInfo.getMessageId())
                        .receiver(taskInfo.getReceiver()).state(AnchorStateEnum.NIGHT_SHIELD.getCode()).build());
            }
            if (taskInfo.getShieldType().equals(ShieldTypeEnum.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode())) {
                redisUtils.lPush(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_REDIS_KEY, JSON.toJSONString(taskInfo, SerializerFeature.WriteClassName), SECONDS_OF_A_DAY);
                logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId())
                        .bizId(taskInfo.getBizId()).messageId(taskInfo.getMessageId())
                        .receiver(taskInfo.getReceiver()).state(AnchorStateEnum.NIGHT_SHIELD_NEXT_SEND.getCode()).build());
            }
        }
    }
}
