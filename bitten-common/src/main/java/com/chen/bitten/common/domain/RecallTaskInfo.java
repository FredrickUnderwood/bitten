package com.chen.bitten.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecallTaskInfo {

    private Long messageTemplateId;

    private List<String> recallMessageIdList;

    private Integer sendAccount;

    private Integer sendChannel;
}
