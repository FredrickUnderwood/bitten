package com.chen.bitten.common.dto;

import com.chen.bitten.common.domain.MessageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendRequest {

    /**
     * send: 发送
     * recall: 撤回
     * 用于标记流程
     */
    private String code;

    private Long messageTemplateId;

    private MessageParam messageParam;

    private List<String> recallMessageIdList;
}
