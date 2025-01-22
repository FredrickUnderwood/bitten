package com.chen.bitten.common.domain;


import com.chen.bitten.common.dto.model.ContentModel;
import com.chen.bitten.common.process.ProcessModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * 发送任务的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskInfo implements ProcessModel, Serializable {


    /**
     * 业务消息发送Id，用于链路追踪，若不存在，则使用 messageId
     */
    private String bizId;

    /**
     * 消息唯一Id（用于数据追踪）
     */
    private String messageId;

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 业务Id：模板类型+模板ID+当天日期（用于数据追踪）
     */
    private Long businessId;

    /**
     * 接收者
     */
    private Set<String> receiver;

    /**
     * 发送的Id类型
     */
    private Integer idType;

    /**
     * 发送渠道
     */
    private Integer sendChannel;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 模板类型
     */
    private Integer templateType;

    /**
     * 屏蔽类型
     */
    private Integer shieldType;

    /**
     * 发送文案模型
     */
    private ContentModel contentModel;

    /**
     * 发送账号，例如邮件、短信可有多个发送账号
     */
    private Integer sendAccount;




}
