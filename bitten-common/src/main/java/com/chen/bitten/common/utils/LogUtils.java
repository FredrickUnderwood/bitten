package com.chen.bitten.common.utils;

import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.ObjectInfo;
import com.chen.bitten.common.mq.MqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogUtils {

    private static final String LOG_PREFIX = "[LogUtils]";

    @Autowired
    private MqService mqService;

    @Value("${bitten.business.log.topic.name}")
    private String topic;

    public void print(ObjectInfo info) {
        info.setLogTimeStamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(info));
    }

    public void print(AnchorInfo info) {
        info.setLogTimeStamp(System.currentTimeMillis());
        String content = JSON.toJSONString(info);
        log.info(content);
        try {
            mqService.send(topic, content);
        } catch (Exception e) {
            log.error("{}Send mq fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
    }

    public void print(ObjectInfo objectInfo, AnchorInfo anchorInfo) {
        print(objectInfo);
        print(anchorInfo);
    }

    public void print(String bizId, String bizType, String msg) {
        log.info("bizId: {}, bizType: {}, msg: {}", bizId, bizType, msg);
    }
}
