package com.chen.bitten.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体日志信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectInfo {

    /**
     * 对象实体
     */
    private Object object;

    /**
     * 标识日志业务
     */
    private String bizType;

    /**
     * 日志生成时间戳
     */
    private Long logTimeStamp;
}
