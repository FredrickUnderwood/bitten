package com.chen.bitten.common.utils;

import com.chen.bitten.common.config.ThreadPoolAutoShutdown;
import org.dromara.dynamictp.core.DtpRegistry;
import org.dromara.dynamictp.core.executor.DtpExecutor;
import org.dromara.dynamictp.core.support.ExecutorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolUtils {
    private static final String SOURCE_NAME = "bitten";
    @Autowired
    private ThreadPoolAutoShutdown threadPoolAutoShutdown;

    /**
     * 1. 当前线程池加入到动态线程池内
     * 2. 注册该线程池到Spring容器中，被Spring管理，能在Spring容器关闭时销毁
     * @param dtpExecutor
     */
    public void register(DtpExecutor dtpExecutor) {
        DtpRegistry.registerExecutor(ExecutorWrapper.of(dtpExecutor), SOURCE_NAME);
        threadPoolAutoShutdown.registerExecutor(dtpExecutor);
    }

}
