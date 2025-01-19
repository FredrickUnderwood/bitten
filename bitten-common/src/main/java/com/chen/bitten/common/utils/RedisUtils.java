package com.chen.bitten.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RedisUtils {

    private static final String LOG_PREFIX = "[RedisUtils]";

    @Autowired
    private RedisTemplate redisTemplate;


    public Map<String, String> mGet(List<String> keys) {
        HashMap<String, String> result = new HashMap<>(keys.size());
        try {
            List<String> values = redisTemplate.opsForValue().multiGet(keys);
            if (Objects.nonNull(values) && !values.isEmpty()) {
                for (int i = 0; i < keys.size(); i++) {
                    if (Objects.nonNull(values.get(i))) {
                        result.put(keys.get(i), values.get(i));
                    }
                }
            }
        } catch (Exception e) {
            log.error("{}multiGet fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
        return result;
    }

    public void pipelineSetEx(Map<String, String> keyValues, Long seconds) {
        try {
            redisTemplate.executePipelined((RedisCallback<String>) connection -> {
                RedisStringCommands command = connection.stringCommands();
                for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                    command.setEx(entry.getKey().getBytes(StandardCharsets.UTF_8), seconds,
                            entry.getValue().getBytes(StandardCharsets.UTF_8));
                }
                return null;
            });
        } catch (Exception e) {
            log.error("{}pipelineSetEx fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
    }

    public void lPush(String key, String value, Long seconds) {
        try {
            redisTemplate.executePipelined((RedisCallback<String>) connection -> {
                RedisListCommands listCommand = connection.listCommands();
                listCommand.lPush(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
                RedisKeyCommands keyCommand = connection.keyCommands();
                keyCommand.expire(key.getBytes(StandardCharsets.UTF_8), seconds);
                return null;
            });
        } catch (Exception e) {
            log.error("{}lPush fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
    }
    public List<String> lRange(String key, int start, int end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("{}lRange fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
        return new ArrayList<>();
    }

    public Boolean executeContentDeduplicateScript(RedisScript<Long> redisScript, List<String> keys, String... args) {
        String[] argsArray = args != null ? args : new String[0];
        try {
            Long execute = (Long) redisTemplate.execute(redisScript, keys, (Object[]) argsArray);
            if (Objects.isNull(execute)) {
                return false;
            }
            return execute.intValue() == 1;
        } catch (Exception e) {

            log.error("{}executeContentDeduplicateScript fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
        return false;
    }
}
