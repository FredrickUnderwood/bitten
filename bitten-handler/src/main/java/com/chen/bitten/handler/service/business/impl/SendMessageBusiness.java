package com.chen.bitten.handler.service.business.impl;

import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.handler.handler.HandlerHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMessageBusiness implements BusinessProcess<TaskInfo> {

    @Autowired
    private HandlerHolder handlerHolder;
    @Override
    public void process(ProcessContext<TaskInfo> context) {
        TaskInfo taskInfo = context.getProcessModel();
        handlerHolder.route(taskInfo.getSendChannel()).doHandler(taskInfo);
    }
}
