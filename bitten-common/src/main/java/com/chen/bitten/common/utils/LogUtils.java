package com.chen.bitten.common.utils;

import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.ObjectInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogUtils {
    public void print(ObjectInfo info) {
        info.setLogTimeStamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(info));
    }

    public void print(AnchorInfo info) {
        info.setLogTimeStamp(System.currentTimeMillis());
        String content = JSON.toJSONString(info);
        log.info(content);
        // TODO 发送MQ逻辑
    }

    public void print(ObjectInfo objectInfo, AnchorInfo anchorInfo) {
        print(objectInfo);
        print(anchorInfo);
    }

    public void print(String bizId, String bizType, String msg) {
        log.info("bizId: {}, bizType: {}, msg: {}", bizId, bizType, msg);
    }
}
