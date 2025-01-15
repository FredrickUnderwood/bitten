package com.chen.bitten.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeduplicateTypeEnum implements BittenEnum {
    /**
     * CONTENT_DEDUPLICATION: 消息被内容去重（重复内容5min内多次发送）
     */
    CONTENT(10, "N分钟相同内容去重"),

    /**
     * 息被频次去重（同一个渠道短时间内发送多次消息给用户）
     */
    FREQUENCY(20, "一天内N次相同渠道去重"),
    ;
    private final Integer code;
    private final String description;
}
