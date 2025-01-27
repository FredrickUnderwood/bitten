package com.chen.bitten.data.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.constant.BittenConstant;
import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.TraceAnchorInfo;
import com.chen.bitten.data.service.ConsumeService;
import com.chen.bitten.data.utils.RedisUtils;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("logConsumeService")
public class ConsumeServiceImpl implements ConsumeService {


    private static final String LOG_PREFIX = "[ConsumeServiceImpl]";

    @Override
    public void consumeLog(AnchorInfo anchorInfo) {
        try {
            RedisUtils.pipeline(redisAsyncCommands -> {
                List<RedisFuture<?>> futures = new ArrayList<>();
                /*
                messageId维度数据
                 */
                String messageIdRedisKey = BittenConstant.REDIS_KEY_PREFIX + anchorInfo.getMessageId();
                TraceAnchorInfo traceAnchorInfo = TraceAnchorInfo.builder()
                        .businessId(anchorInfo.getBusinessId())
                        .logTimeStamp(anchorInfo.getLogTimeStamp())
                        .status(anchorInfo.getState()).build();
                futures.add(redisAsyncCommands.lpush(messageIdRedisKey.getBytes(StandardCharsets.UTF_8), JSON.toJSONString(traceAnchorInfo).getBytes(StandardCharsets.UTF_8)));
                futures.add(redisAsyncCommands.expire(messageIdRedisKey.getBytes(StandardCharsets.UTF_8), Duration.ofDays(1L).toMillis() / 1000));
                /*
                receiver维度数据
                 */
                for (String receiver: anchorInfo.getReceiver()) {
                    futures.add(redisAsyncCommands.lpush(receiver.getBytes(StandardCharsets.UTF_8), JSON.toJSONString(traceAnchorInfo).getBytes(StandardCharsets.UTF_8)));
                    futures.add(redisAsyncCommands.expire(receiver.getBytes(StandardCharsets.UTF_8), (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000));
                }
                /*
                模板维度（businessId）
                 */
                futures.add(redisAsyncCommands.hincrby(String.valueOf(anchorInfo.getBusinessId()).getBytes(StandardCharsets.UTF_8),
                        String.valueOf(anchorInfo.getState()).getBytes(StandardCharsets.UTF_8), anchorInfo.getReceiver().size()));
                futures.add(redisAsyncCommands.expire(String.valueOf(anchorInfo.getBusinessId()).getBytes(StandardCharsets.UTF_8),
                        DateUtil.offsetDay(new Date(), 30).getTime() / 1000 - DateUtil.currentSeconds()));
                return futures;
            });
        } catch (Exception e) {
            log.error("{}consumeLog fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }

    }
}
