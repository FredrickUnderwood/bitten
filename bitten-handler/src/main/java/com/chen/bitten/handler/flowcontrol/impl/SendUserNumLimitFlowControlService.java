package com.chen.bitten.handler.flowcontrol.impl;

import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.RateLimitStrategy;
import com.chen.bitten.handler.flowcontrol.FlowControlParam;
import com.chen.bitten.handler.flowcontrol.FlowControlService;
import com.chen.bitten.handler.flowcontrol.annotations.RateLimit;
import com.google.common.util.concurrent.RateLimiter;

@RateLimit(rateLimitStrategy = RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT)
public class SendUserNumLimitFlowControlService implements FlowControlService {
    @Override
    public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(taskInfo.getReceiver().size());
    }
}
