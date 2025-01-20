package com.chen.bitten.common.enums;

import com.chen.bitten.common.dto.model.ContentModel;
import com.chen.bitten.common.dto.model.EmailContentModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ChannelTypeEnum implements BittenEnum {

    // TODO 发送文案模型未扩展 accessToken相关未扩展
    EMAIL(10, "电子邮件", "email", EmailContentModel.class);
    private final Integer code;
    private final String description;
    private final String codeEn;
    private final Class<? extends ContentModel> contentModelClass;

    /**
     *
     * @param code: sendChannel
     * @return
     */
    public static Class<? extends ContentModel> getContentModelClassByCode (Integer code) {
        return Arrays.stream(ChannelTypeEnum.class.getEnumConstants())
                .filter(channelType -> channelType.getCode().equals(code))
                .map(ChannelTypeEnum::getContentModelClass)
                .findFirst().orElse(null);
    }
}
