package com.chen.bitten.common.process;

import com.chen.bitten.common.vo.BasicResultVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessContext <T extends ProcessModel> implements Serializable {
    /**
     * 标识流程的id
     */
    private String code;

    /**
     * 记录流程信息的实体，如TaskInfo
     */
    private T processModel;

    private Boolean needBreak;

    private BasicResultVO response;
}
