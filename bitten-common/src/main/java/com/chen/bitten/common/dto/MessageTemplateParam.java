package com.chen.bitten.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageTemplateParam {

    private Integer page = 1;

    private Integer pageSize = 10;

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 当前用户
     */
    private String creator;

    /**
     * 支持模糊搜索
     */
    private String name;

    /**
     * 测试用信息
     */
    private String messageContent;

    /**
     * 测试用信息
     */
    private String receivers;



}
