package com.chen.bitten.handler.deduplicate;

import com.alibaba.fastjson.annotation.JSONField;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.AnchorStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeduplicateParam {

    /**
     * TaskInfo信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间，单位：秒
     */
    @JSONField(name = "time")
    private Long deduplicateTime;

    /**
     * 去重次数
     */
    @JSONField(name = "num")
    private Integer deduplicateNum;

    /**
     * 去重类型
     * CONTENT_DEDUPLICATION: 消息被内容去重（重复内容5min内多次发送）
     * FREQUENCY_DEDUPLICATION: 消息被频次去重（同一个渠道短时间内发送多次消息给用户）
     */
    private AnchorStateEnum anchorState;
}
