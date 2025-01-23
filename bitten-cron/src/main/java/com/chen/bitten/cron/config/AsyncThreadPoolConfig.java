package com.chen.bitten.cron.config;

import cn.hutool.core.thread.ExecutorBuilder;
import com.chen.bitten.common.constant.ThreadPoolConstant;
import org.dromara.dynamictp.common.em.QueueTypeEnum;
import org.dromara.dynamictp.common.em.RejectedTypeEnum;
import org.dromara.dynamictp.core.executor.DtpExecutor;
import org.dromara.dynamictp.core.support.ThreadPoolBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncThreadPoolConfig {

    public static final String THREAD_POOL_NAME = "async_thread_pool";

    private AsyncThreadPoolConfig() {

    }


    /**
     * 实际发送mq给handler的线程池
     */
    public static ExecutorService getConsumePendingTasksExecutor() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.COMMON_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.COMMON_MAXIMUM_POOL_SIZE)
                .setWorkQueue(new LinkedBlockingQueue<>(ThreadPoolConstant.BLOCKING_QUEUE_SIZE))
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setAllowCoreThreadTimeOut(true)
                .setKeepAliveTime(ThreadPoolConstant.SINGLE_KEEP_ALIVE_TIME, TimeUnit.SECONDS).build();
    }

    /**
     * 接收xxl-job的线程池
     * @return
     */
    public static DtpExecutor getXxlJobExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName(THREAD_POOL_NAME)
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
