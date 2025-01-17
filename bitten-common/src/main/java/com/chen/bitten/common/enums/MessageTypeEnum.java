package com.chen.bitten.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTypeEnum implements BittenEnum{
    NOTICE(10, "通知类消息", "notice");
    private final Integer code;
    private final String description;
    private final String codeEn;
}
