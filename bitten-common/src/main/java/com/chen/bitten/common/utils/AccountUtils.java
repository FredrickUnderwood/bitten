package com.chen.bitten.common.utils;

import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.domain.persistence.ChannelAccount;
import com.chen.bitten.server.mapper.ChannelAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Slf4j
@Configuration
public class AccountUtils {

    private final static String LOG_PREFIX = "[AccountUtils]";

    @Autowired
    private ChannelAccountMapper channelAccountMapper;

    public <T> T getAccountById(Integer accountId, Class<T> clazz) {
        try {
            ChannelAccount channelAccount = channelAccountMapper.findById(accountId);
            if (channelAccount != null) {
                return JSON.parseObject(channelAccount.getAccountConfig(), clazz);
            }
        } catch (Exception e) {
            log.error("{}getAccountById fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
        return null;
    }
}
