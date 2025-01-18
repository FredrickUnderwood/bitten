package com.chen.bitten.handler.deduplicate.service;

import cn.hutool.core.text.StrPool;
import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.AnchorStateEnum;
import com.chen.bitten.common.enums.DeduplicateTypeEnum;
import com.chen.bitten.common.utils.LogUtils;
import com.chen.bitten.common.utils.RedisUtils;
import com.chen.bitten.handler.deduplicate.DeduplicateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FrequencyDeduplicateService extends AbstractDeduplicateService{

    private static final String FREQUENCY_DEDUPLICATE_PREFIX = "FRE_";

    private static final String FREQUENCY_DEDUPLICATE_REDIS_PREFIX = "FD_";

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private LogUtils logUtils;

    public FrequencyDeduplicateService() {
        this.DEDUPLICATE_TYPE = DeduplicateTypeEnum.FREQUENCY.getCode();
    }

    @Override
    public void deduplicate(DeduplicateParam deduplicateParam) {
        TaskInfo taskInfo = deduplicateParam.getTaskInfo();
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        List<String> readyPutRedisKeys = new ArrayList<>(taskInfo.getReceiver().size());
        List<String> keys = generateFrequencyDeduplicateKey(taskInfo).stream().map(key -> FREQUENCY_DEDUPLICATE_REDIS_PREFIX + key).toList();
        Map<String, String> inRedisKeyValues = redisUtils.mGet(keys);
        for (String receiver: taskInfo.getReceiver()) {
            String key = FREQUENCY_DEDUPLICATE_REDIS_PREFIX + generateSingleFrequencyDeduplicateKey(taskInfo, receiver);
            String value = inRedisKeyValues.get(key);
            if (Objects.nonNull(value) && Integer.parseInt(value) >= deduplicateParam.getDeduplicateNum()) {
                filterReceiver.add(receiver);
            } else {
                readyPutRedisKeys.add(key);
            }
        }
        putInRedis(readyPutRedisKeys, inRedisKeyValues, deduplicateParam.getDeduplicateTime());
        if (!filterReceiver.isEmpty()) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            logUtils.print(AnchorInfo.builder().messageId(taskInfo.getMessageId()).bizId(taskInfo.getBizId())
                    .receiver(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId())
                    .state(deduplicateParam.getAnchorState().getCode()).build());
        }
    }

    private void putInRedis(List<String> readyPutRedisKeys,
                            Map<String, String> inRedisKeyValues, Long deduplicateTime) {
        Map<String, String> keyValues = new HashMap<>(readyPutRedisKeys.size());
        for (String key: readyPutRedisKeys) {
            if (Objects.nonNull(inRedisKeyValues.get(key))) {
                keyValues.put(key, String.valueOf(Integer.parseInt(inRedisKeyValues.get(key)) + 1));
            } else {
                keyValues.put(key, String.valueOf(1));
            }
        }
        if (!keyValues.isEmpty()) {
            redisUtils.pipelineSetEx(keyValues, deduplicateTime);
        }
    }


    private List<String> generateFrequencyDeduplicateKey(TaskInfo taskInfo) {
        List<String> allKeys = new ArrayList<>(taskInfo.getReceiver().size());
        for (String receiver: taskInfo.getReceiver()) {
            allKeys.add(generateSingleFrequencyDeduplicateKey(taskInfo, receiver));
        }
        return allKeys;
    }

    /**
     * FRE_receiver_channel
     * @param taskInfo
     * @param receiver
     * @return
     */
    private String generateSingleFrequencyDeduplicateKey(TaskInfo taskInfo, String receiver) {
        return FREQUENCY_DEDUPLICATE_PREFIX + StrPool.C_UNDERLINE
                + receiver + StrPool.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
