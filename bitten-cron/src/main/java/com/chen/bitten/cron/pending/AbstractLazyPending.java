package com.chen.bitten.cron.pending;

import com.chen.bitten.cron.config.ThreadPoolConfig;
import com.chen.bitten.common.domain.PendingParam;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Setter
abstract public class AbstractLazyPending<T> {

    private static final String LOG_PREFIX = "[AbstractLazyPending]";

    protected PendingParam<T> pendingParam;

    private List<T> tasks = new ArrayList<>();

    private Long lastHandledTime = System.currentTimeMillis();

    /**
     * 是否终止线程
     */
    private volatile Boolean stop = false;

    @PostConstruct
    public void initConsumePending() {
        ExecutorService executorService = ThreadPoolConfig.getSinglePendingThreadPool();
        executorService.execute(() -> {
            while (true) {
                try {
                    T obj = pendingParam.getQueue().poll(pendingParam.getTimeThreshold(), TimeUnit.MILLISECONDS);
                    if (obj != null) {
                        tasks.add(obj);
                    }
                    if (stop.equals(true) && tasks.isEmpty()) {
                        executorService.shutdown();
                        break;
                    }
                    // 处理条件：数量超限制或时间超限制
                    if (!tasks.isEmpty() && ready()) {
                        List<T> taskRef = tasks;
                        tasks = Lists.newArrayList();
                        lastHandledTime = System.currentTimeMillis();
                        // 执行具体逻辑
                        pendingParam.getExecutorService().execute(() -> this.handle(taskRef));
                    }
                } catch (InterruptedException e) {
                    log.error("{}initConsumePending fail! e: {}", LOG_PREFIX, e.getStackTrace());
                    Thread.currentThread().interrupt();
                }

            }
        });
    }

    public boolean ready() {
        return tasks.size() >= pendingParam.getBatchThreshold() || System.currentTimeMillis() - lastHandledTime >= pendingParam.getTimeThreshold();
    }

    /**
     * 将元素放入阻塞队列
     */
    public void pending(T task) {
        try {
            pendingParam.getQueue().put(task);
        } catch (InterruptedException e) {
            log.error("{}pending fail! e: {}", LOG_PREFIX, e.getStackTrace());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 消费阻塞队列元素
     */
    public void handle(List<T> tasks) {
        if (tasks.isEmpty()) {
            return;
        }
        try {
            doHandle(tasks);
        } catch (Exception e) {
            log.error("{}handle fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
    }


    /**
     * 处理阻塞队列元素，真正干活的方法
     */
    public abstract void doHandle(List<T> tasks);
}
