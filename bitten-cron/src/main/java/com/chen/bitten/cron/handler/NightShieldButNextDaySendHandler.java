package com.chen.bitten.cron.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.mq.MqService;
import com.chen.bitten.common.utils.RedisUtils;
import com.chen.bitten.common.utils.ThreadPoolUtils;
import com.chen.bitten.cron.config.ThreadPoolConfig;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
public class NightShieldButNextDaySendHandler {

    private static final String LOG_PREFIX = "[NightShieldButNextDaySendHandler]";

    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_REDIS_KEY = "night_shield_but_next_day_send";

    @Value("${bitten.business.topic.name}")
    private String topic;
    @Value("${bitten.business.tagId.value}")
    private String tagId;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MqService mqService;

    @XxlJob("nightShieldButNextDaySend")
    public void execute() {
        ThreadPoolConfig.getSinglePendingThreadPool().execute(() -> {
            while (redisUtils.lLen(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_REDIS_KEY) > 0) {
                String taskInfo = redisUtils.lPop(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_REDIS_KEY);
                if (Objects.nonNull(taskInfo) && !taskInfo.isBlank()) {
                    try {
                        mqService.send(topic, JSON.toJSONString(Collections.singletonList(JSON.parseObject(taskInfo, TaskInfo.class)), SerializerFeature.WriteClassName));
                    } catch (Exception e) {
                        log.error("{}execute fail! e: {}", LOG_PREFIX, e.getStackTrace());
                    }

                }
            }
        });
    }
}
