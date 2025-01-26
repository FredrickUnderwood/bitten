package com.chen.bitten.cron.config;

import cn.hutool.core.thread.ExecutorBuilder;
import com.chen.bitten.common.constant.ThreadPoolConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolConfig {
    private ThreadPoolConfig() {

    }
    public static ExecutorService getSinglePendingThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.SINGLE_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.SINGLE_MAXIMUM_POOL_SIZE)
                .setKeepAliveTime(ThreadPoolConstant.SINGLE_KEEP_ALIVE_TIME, TimeUnit.SECONDS)
                .setWorkQueue(new LinkedBlockingQueue<>(ThreadPoolConstant.BLOCKING_QUEUE_SIZE))
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setAllowCoreThreadTimeOut(true)
                .build();
    }
}
