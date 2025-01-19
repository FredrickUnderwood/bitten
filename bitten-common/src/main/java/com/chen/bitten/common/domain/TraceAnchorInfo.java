package com.chen.bitten.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraceAnchorInfo {

    /**
     * 具体点位
     */
    private Integer status;
    /**
     * 业务Id：模板类型 + 模板ID + 当天日期（用于数据追踪）
     */
    private Long businessId;
    /**
     * 日志生成时间戳
     */
    private Long logTimeStamp;
}
