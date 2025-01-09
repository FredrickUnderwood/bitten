package com.chen.bitten.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ThreadPoolAutoShutdown implements ApplicationListener<ContextClosedEvent> {

    private static final String LOG_PREFIX = "[ThreadPoolAutoShutdown]";
    /**
     * the maximum time to wait
     */
    private static final long TIME_OUT = 20;
    /**
     * the time unit of the timeout argument
     */
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    /**
     * final关键字修饰，保证线程安全
     */
    private final List<ExecutorService> POOLS = Collections.synchronizedList(new ArrayList<>(12));

    public void registerExecutor(ExecutorService pool) {
        POOLS.add(pool);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("{}Thread pool shut down, thread pool size: {}", LOG_PREFIX, POOLS.size());
        if (CollectionUtils.isEmpty(POOLS)) {
            return;
        }
        for (ExecutorService pool: POOLS) {
            /*
              shutdown()停止接收新任务，原先提交的任务继续执行
             */
            pool.shutdown();
            try {
                /*
                  awaitTermination()当前线程阻塞，直到：
                  - 所有已提交的任务执行完
                  - 超时时间到
                  - 线程被中断
                 */
                if (!pool.awaitTermination(TIME_OUT, UNIT)) {
                    log.warn("{}Timeout while waiting for thread pool: {} to terminate", LOG_PREFIX, pool);
                }
            } catch (InterruptedException e) {
                log.warn("{}Timeout while waiting for thread pool: {} to terminate", LOG_PREFIX, pool);
                Thread.currentThread().interrupt();
            }
        }
    }



}
