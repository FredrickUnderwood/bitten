package com.chen.bitten.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageParam {


    private String bizId;

    /**
     * 多个receiver用逗号分隔
     */
    private String receivers;

    /**
     * 需要替换的内瓤
     */
    private Map<String, String> variables;

    /**
     * 扩展参数
     */
    private Map<String, String> extras;
}
