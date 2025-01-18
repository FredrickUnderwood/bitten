package com.chen.bitten.handler.deduplicate.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.DeduplicateTypeEnum;
import com.chen.bitten.common.utils.LogUtils;
import com.chen.bitten.common.utils.RedisUtils;
import com.chen.bitten.handler.deduplicate.DeduplicateParam;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContentDeduplicateService extends AbstractDeduplicateService {

    private static final String CONTENT_DEDUPLICATE_REDIS_PREFIX = "CD_";

    private static final String CONTENT_DEDUPLICATE_SCRIPT_PATH = "lua/content_deduplicate_script.lua";

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private LogUtils logUtils;

    private DefaultRedisScript redisScript;

    public ContentDeduplicateService() {
        this.DEDUPLICATE_TYPE = DeduplicateTypeEnum.CONTENT.getCode();
    }

    @PostConstruct
    public void initRedisScript() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(CONTENT_DEDUPLICATE_SCRIPT_PATH)));
        redisScript.setResultType(Long.class);
    }

    @Override
    public void deduplicate(DeduplicateParam deduplicateParam) {
        TaskInfo taskInfo = deduplicateParam.getTaskInfo();
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        for (String receiver: taskInfo.getReceiver()) {
            String key = CONTENT_DEDUPLICATE_REDIS_PREFIX + generateSingleContentDeduplicateKey(taskInfo, receiver);
            String score = String.valueOf(System.currentTimeMillis());
            String scoreValue = String.valueOf(IdUtil.getSnowflake().nextId());
            final Boolean result = redisUtils.executeContentDeduplicateScript(redisScript, Collections.singletonList(key),
                    String.valueOf(deduplicateParam.getDeduplicateTime() * 1000),
                    score, String.valueOf(deduplicateParam.getDeduplicateNum()), scoreValue);
            if (Boolean.TRUE.equals(result)) {
                filterReceiver.add(receiver);
            }
        }
        if (!filterReceiver.isEmpty()) {
            taskInfo.getReceiver().removeAll(filterReceiver);
            logUtils.print(AnchorInfo.builder().messageId(taskInfo.getMessageId()).bizId(taskInfo.getBizId())
                    .receiver(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId())
                    .state(deduplicateParam.getAnchorState().getCode()).build());
        }
    }

    /**
     * MessageTemplateId + receiver + contentModel
     * @param taskInfo
     * @param receiver
     * @return
     */
    private String generateSingleContentDeduplicateKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(taskInfo.getMessageTemplateId() + receiver + JSON.toJSONString(taskInfo.getContentModel()));
    }
}
