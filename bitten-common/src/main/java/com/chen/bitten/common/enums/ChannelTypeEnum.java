package com.chen.bitten.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChannelTypeEnum implements BittenEnum{

    // TODO 发送文案模型未扩展 accessToken相关未扩展
    EMAIL(10, "电子邮件", "email");
    private final Integer code;
    private final String description;
    private final String codeEn;

}
