package com.chen.bitten.handler.flowcontrol;

import com.chen.bitten.common.enums.RateLimitStrategy;
import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlowControlParam {

    /**
     * 限流器
     */
    protected RateLimiter rateLimiter;

    /**
     * 限流器初始流量大小
     */
    protected Double rateLimitInitValue;

    /**
     * 限流策略选择
     */
    protected RateLimitStrategy rateLimitStrategy;

}
