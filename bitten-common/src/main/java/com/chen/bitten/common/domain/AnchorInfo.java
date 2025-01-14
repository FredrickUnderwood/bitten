package com.chen.bitten.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 埋点日志信息
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnchorInfo {

    /**
     * 业务消息发送Id，用于链路追踪，若不存在，则使用 messageId
     */
    private String bizId;
    /**
     * 消息唯一Id（用于数据追踪）
     */
    private String messageId;
    /**
     * 接收者
     */
    private Set<String> receiver;
    /**
     * 具体点位: AnchorStateEnum
     */
    private Integer state;
    /**
     * 业务Id：模板类型 + 模板ID + 当天日期（用于数据追踪）
     */
    private Long businessId;
    /**
     * 日志生成时间戳
     */
    private Long logTimeStamp;



}
