package com.chen.bitten.handler.handler.pending;


import com.chen.bitten.common.utils.ThreadPoolUtils;
import com.chen.bitten.handler.config.HandlerThreadPoolConfig;
import com.chen.bitten.handler.utils.GroupIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.core.DtpRegistry;
import org.dromara.dynamictp.core.executor.DtpExecutor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * groupId ——> 具体线程池的映射
 */
@Slf4j
@Component
public class TaskPendingHolder {

    private static List<String> allGroupIds = GroupIdUtils.getAllGroupIds();
    @Autowired
    private ThreadPoolUtils threadPoolUtils;

    /**
     * 每个groupId初始化一个线程池
     */
    @PostConstruct
    public void init() {
        for (String groupId: allGroupIds) {
            DtpExecutor dtpExecutor = HandlerThreadPoolConfig.getExecutor(groupId);
            threadPoolUtils.register(dtpExecutor);
        }
    }

    /**
     * 获得groupId对应的线程池
     */
    public DtpExecutor route(String groupId) {
        return (DtpExecutor) DtpRegistry.getExecutor(HandlerThreadPoolConfig.PREFIX + groupId);
    }
}
