package com.chen.bitten.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RateLimitStrategy implements BittenEnum {
    /**
     * 根据真实请求数限流 (实际意义上的QPS）
     */
    REQUEST_RATE_LIMIT(10, "根据真实请求数限流"),
    /**
     * 根据发送用户数限流（人数限流）
     */
    SEND_USER_NUM_RATE_LIMIT(20, "根据发送用户数限流"),
    ;

    private final Integer code;
    private final String description;
}
