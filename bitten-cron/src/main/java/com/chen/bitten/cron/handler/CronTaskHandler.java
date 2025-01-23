package com.chen.bitten.cron.handler;

import com.chen.bitten.common.utils.ThreadPoolUtils;
import com.chen.bitten.cron.config.AsyncThreadPoolConfig;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.kafka.common.utils.ThreadUtils;
import org.dromara.dynamictp.core.executor.DtpExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CronTaskHandler {

    @Autowired
    private TaskHandler taskHandler;

    @Autowired
    private ThreadPoolUtils threadPoolUtils;
    private final DtpExecutor dtpExecutor = AsyncThreadPoolConfig.getXxlJobExecutor();

    @XxlJob("bittenCronJob")
    public void execute() {
        threadPoolUtils.register(dtpExecutor);
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        dtpExecutor.execute(() -> taskHandler.handle(messageTemplateId));
    }
}
