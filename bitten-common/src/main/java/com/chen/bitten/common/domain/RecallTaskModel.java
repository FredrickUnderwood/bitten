package com.chen.bitten.common.domain;

import com.chen.bitten.common.process.ProcessModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecallTaskModel implements ProcessModel {

    private Long messageTemplateId;

    private List<String> recallMessageIdList;

    private RecallTaskInfo recallTaskInfo;
}
