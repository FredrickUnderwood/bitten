package com.chen.bitten.handler.config;

import com.chen.bitten.common.constant.ThreadPoolConstant;
import org.dromara.dynamictp.common.em.QueueTypeEnum;
import org.dromara.dynamictp.common.em.RejectedTypeEnum;
import org.dromara.dynamictp.core.executor.DtpExecutor;
import org.dromara.dynamictp.core.support.ThreadPoolBuilder;

import java.util.concurrent.TimeUnit;

/**
 * handler模块线程池配置
 */
public class HandlerThreadPoolConfig {

    public static final String PREFIX = "bitten.";

    public static DtpExecutor getExecutor(String groupId) {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName(PREFIX + groupId)
                .corePoolSize(ThreadPoolConstant.COMMON_CORE_POOL_SIZE)
                .maximumPoolSize(ThreadPoolConstant.COMMON_MAXIMUM_POOL_SIZE)
                .keepAliveTime(ThreadPoolConstant.COMMON_KEEP_ALIVE_TIME)
                .timeUnit(TimeUnit.SECONDS)
                .workQueue(QueueTypeEnum.VARIABLE_LINKED_BLOCKING_QUEUE.getName(), ThreadPoolConstant.COMMON_QUEUE_CAPACITY, false)
                .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                .allowCoreThreadTimeOut(false)
                .buildDynamic();
    }
}