package com.chen.bitten.common.domain.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 消息模板
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class MessageTemplate implements Serializable {

    private Long id;

    /**
     * 模板标题
     */
    private String name;

    /**
     * 当前消息审核状态： 10.待审核 20.审核成功 30.被拒绝
     */
    private Integer auditStatus;

    /**
     * 工单id（审核模板走工单）
     */
    private String flowId;

    /**
     * 当前消息状态：10.新建 20.停用 30.启用 40.等待发送 50.发送中 60.发送成功 70.发送失败
     */
    private Integer messageStatus;

    /**
     * 定时任务id
     */
    private Integer cronTaskId;

    /**
     * 定时发送人群的文件路径
     */
    private String cronCrowdPath;

    /**
     * 期望发送时间：0:立即发送 定时任务以及周期任务:cron表达式
     */
    private String expectPushTime;

    /**
     * 消息的发送ID类型：10. userId 20.did 30.手机号 40.openId 50.email 60.企业微信userId
     */
    private Integer idType;
    /**
     * 消息发送渠道：10.IM 20.Push 30.短信 40.Email 50.公众号 60.小程序 70.企业微信 80.钉钉机器人 90.钉钉工作通知 100.企业微信机器人 110.飞书机器人 110.飞书应用消息
     */
    private Integer sendChannel;
    /**
     * 10.运营类 20.技术类接口调用
     */
    private Integer templateType;
    /**
     * 10.通知类消息 20.营销类消息 30.验证码类消息
     */
    private Integer messageType;
    /**
     * 10.夜间不屏蔽 20.夜间屏蔽 30.夜间屏蔽(次日早上9点发送)
     */
    private Integer shieldType;
    /**
     * 消息内容 占位符用{$var}表示
     */
    private String messageContent;
    private Integer sendAccount;
    private String creator;
    private String updater;
    private String auditor;
    /**
     * 业务方团队
     */
    private String team;
    /**
     * 业务方
     */
    private String proposer;
    /**
     * 是否删除：0.不删除 1.删除
     */
    private Integer isDeleted;
    private Integer createdTime;
    private Integer updatedTime;


}
