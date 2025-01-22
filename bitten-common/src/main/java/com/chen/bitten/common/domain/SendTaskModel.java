package com.chen.bitten.common.domain;

import com.chen.bitten.common.process.ProcessModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Api层使用的ProcessModel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTaskModel implements ProcessModel {

    private Long messageTemplateId;

    private List<MessageParam> messageParamList;

    private List<TaskInfo> taskInfoList;

}
