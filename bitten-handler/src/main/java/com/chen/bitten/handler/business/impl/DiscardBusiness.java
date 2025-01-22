package com.chen.bitten.handler.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.chen.bitten.common.config.ConfigCenter;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.AnchorStateEnum;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 根据模板id丢弃消息
 */
@Service
public class DiscardBusiness implements BusinessProcess<TaskInfo> {

    private static final String DISCARD_MESSAGE_KEY = "discardMsgIds";

    @Autowired
    private ConfigCenter configCenter;

    @Autowired
    private LogUtils logUtils;

    @Override
    public void process(ProcessContext<TaskInfo> context) {
        TaskInfo taskInfo = context.getProcessModel();
        JSONArray array = JSON.parseArray(configCenter.getProperty(DISCARD_MESSAGE_KEY, CommonConstant.EMPTY_JSON_ARRAY));
        if (array.contains(taskInfo.getMessageTemplateId())) {
            logUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId())
                    .receiver(taskInfo.getReceiver()).bizId(taskInfo.getBizId())
                    .messageId(taskInfo.getMessageId()).state(AnchorStateEnum.DISCARD.getCode()).build());
            context.setNeedBreak(true);
        }

    }
}
