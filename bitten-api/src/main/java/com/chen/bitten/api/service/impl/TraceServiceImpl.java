package com.chen.bitten.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.chen.bitten.api.service.TraceService;
import com.chen.bitten.common.constant.BittenConstant;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.TraceAnchorInfo;
import com.chen.bitten.common.dto.TraceResponse;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TraceServiceImpl implements TraceService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public TraceResponse traceByMessageId(String messageId) {
        if (Objects.isNull(messageId) || messageId.isBlank()) {
            return new TraceResponse(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg(), null);
        }
        String redisKey = BittenConstant.REDIS_KEY_PREFIX + messageId;
        List<String> traceAnchorInfoList = redisUtils.lRange(redisKey, 0, -1);
        if (Objects.isNull(traceAnchorInfoList) || traceAnchorInfoList.isEmpty()) {
            return new TraceResponse(RespStatusEnum.FAIL.getCode(), RespStatusEnum.FAIL.getMsg(), null);
        }
        List<TraceAnchorInfo> sortedTraceAnchorInfoList = traceAnchorInfoList.stream().map(traceAnchorInfo -> JSON.parseObject(traceAnchorInfo, TraceAnchorInfo.class))
                .sorted((traceAnchorInfo1, traceAnchorInfo2) -> Math.toIntExact(traceAnchorInfo1.getLogTimeStamp() - traceAnchorInfo2.getLogTimeStamp()))
                .collect(Collectors.toList());
        return new TraceResponse(RespStatusEnum.SUCCESS.getCode(), RespStatusEnum.SUCCESS.getMsg(), sortedTraceAnchorInfoList);
    }
}
