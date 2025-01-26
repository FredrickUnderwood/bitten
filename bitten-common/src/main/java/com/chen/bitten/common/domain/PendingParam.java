package com.chen.bitten.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PendingParam<T> {

    /**
     * 线程池实例
     */
    protected ExecutorService executorService;
    /**
     * 阻塞队列实例
     */
    protected BlockingDeque<T> queue;
    /**
     * batch阈值
     */
    protected Integer batchThreshold;
    /**
     * 时间阈值
     */
    protected Long timeThreshold;
}
