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
public class BatchSendRequest {

    private String code;

    private Long messageTemplateId;

    private List<MessageParam> messageParamList;
}
