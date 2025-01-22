package com.chen.bitten.handler.flowcontrol.impl;

import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.RateLimitStrategy;
import com.chen.bitten.handler.flowcontrol.FlowControlParam;
import com.chen.bitten.handler.flowcontrol.FlowControlService;
import com.chen.bitten.handler.flowcontrol.annotations.RateLimit;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

@RateLimit(rateLimitStrategy = RateLimitStrategy.REQUEST_RATE_LIMIT)
public class RequestRateLimitFlowControlService implements FlowControlService {
    @Override
    public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter = flowControlParam.getRateLimiter();
        return rateLimiter.acquire(1);
    }
}
