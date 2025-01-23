package com.chen.bitten.cron.xxl.utils;

import cn.hutool.core.date.DateUtil;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.cron.xxl.constant.XxlJobConstant;
import com.chen.bitten.cron.xxl.service.CronTaskService;
import com.chen.bitten.cron.xxl.domain.XxlJobGroup;
import com.chen.bitten.cron.xxl.domain.XxlJobInfo;
import com.chen.bitten.cron.xxl.enums.ExecutorBlockStrategyEnum;
import com.chen.bitten.cron.xxl.enums.ExecutorRouteStrategyEnum;
import com.chen.bitten.cron.xxl.enums.GlueTypeEnum;
import com.chen.bitten.cron.xxl.enums.MisfireStrategyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class XxlJobUtils {

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.title}")
    private String title;

    @Autowired
    private CronTaskService cronTaskService;

    public XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {
        String scheduledConf = messageTemplate.getExpectPushTime();
        /*
        如果没有cron表达式，说明立即执行
         */
        if (scheduledConf.equals(String.valueOf(0))) {
            scheduledConf = DateUtil.format(DateUtil.offsetSecond(new Date(), XxlJobConstant.INSTANT_JOB_DELAY_SECONDS), CommonConstant.CRON_FORMAT);
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder()
                .jobGroup(queryGroupId())
                .jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(scheduledConf)
                .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
                .executorRouteStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.JOB_HANDLER_NAME)
                .executorParam(String.valueOf(messageTemplate.getId()))
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                .executorTimeout(XxlJobConstant.TIMEOUT_SECONDS)
                .executorTimeout(XxlJobConstant.RETRY_COUNT)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(0)
                .glueRemark(CommonConstant.EMPTY_STRING)
                .glueSource(CommonConstant.EMPTY_STRING)
                .alarmEmail(CommonConstant.EMPTY_STRING)
                .childJobId(CommonConstant.EMPTY_STRING).build();

        if (Objects.nonNull(messageTemplate.getCronTaskId())) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }

    public Integer queryGroupId() {
        BasicResultVO<Integer> basicResultVO = cronTaskService.getGroupId(appname, title);
        if (Objects.isNull(basicResultVO.getData())) {
            XxlJobGroup xxlJobGroup = XxlJobGroup.builder().appname(appname).title(title).addressType(0).build();
            if (RespStatusEnum.SUCCESS.getCode().equals(cronTaskService.createGroup(xxlJobGroup).getStatus())) {
                return (Integer) cronTaskService.getGroupId(appname, title).getData();
            }
        }
        return basicResultVO.getData();
    }


}
