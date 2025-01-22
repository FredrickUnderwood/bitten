package com.chen.bitten.common.process;

import com.chen.bitten.common.vo.BasicResultVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProcessContext <T extends ProcessModel> implements Serializable {
    /**
     * 标识流程的id
     */
    private String code;

    /**
     * 记录流程信息的实体，如TaskInfo
     */
    private T processModel;

    /**
     * 需要丢弃的消息needBreak置为true
     */
    private Boolean needBreak;

    private BasicResultVO response;
}
