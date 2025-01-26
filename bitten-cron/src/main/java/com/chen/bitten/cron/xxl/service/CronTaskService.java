package com.chen.bitten.cron.xxl.service;

import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.cron.xxl.domain.XxlJobGroup;
import com.chen.bitten.cron.xxl.domain.XxlJobInfo;

public interface CronTaskService {

    BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo);

    BasicResultVO deleteCronTask(Integer taskId);

    BasicResultVO startCronTask(Integer taskId);

    BasicResultVO stopCronTask(Integer taskId);

    /**
     * 得到执行器Id
     * @param appname
     * @param title
     * @return
     */
    BasicResultVO getGroupId(String appname, String title);

    /**
     * 创建执行器
     * @param xxlJobGroup
     * @return
     */
    BasicResultVO createGroup(XxlJobGroup xxlJobGroup);
}
