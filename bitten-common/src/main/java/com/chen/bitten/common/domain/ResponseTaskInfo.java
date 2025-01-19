package com.chen.bitten.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTaskInfo {

    private String bizId;

    /**
     * 消息唯一Id（用于数据追踪）
     */
    private String messageId;

    /**
     * 业务Id：模板类型+模板ID+当天日期（用于数据追踪）
     */
    private Long businessId;

}
