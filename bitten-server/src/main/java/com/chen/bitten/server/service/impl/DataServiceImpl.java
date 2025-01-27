package com.chen.bitten.server.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.chen.bitten.api.service.TraceService;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.TraceAnchorInfo;
import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.dto.TraceResponse;
import com.chen.bitten.common.enums.AnchorStateEnum;
import com.chen.bitten.common.enums.ChannelTypeEnum;
import com.chen.bitten.common.mapper.MessageTemplateMapper;
import com.chen.bitten.common.utils.EnumUtils;
import com.chen.bitten.common.utils.RedisUtils;
import com.chen.bitten.common.utils.TaskInfoUtils;
import com.chen.bitten.common.vo.UserTimeLineVO;
import com.chen.bitten.server.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private TraceService traceService;

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public UserTimeLineVO getMessageTrace(String messageId) {
        TraceResponse response = traceService.traceByMessageId(messageId);
        if (Objects.isNull(response) || Objects.isNull(response.getData()) || response.getData().isEmpty()) {
            return UserTimeLineVO.builder().items(new ArrayList<>()).build();
        }
        return buildUserTimeLineVO(response.getData());

    }

    @Override
    public UserTimeLineVO getUserTrace(String receiver) {
        List<String> userInfoList = redisUtils.lRange(receiver, 0, -1);
        if (Objects.isNull(userInfoList) || userInfoList.isEmpty()) {
            return UserTimeLineVO.builder().items(new ArrayList<>()).build();
        }
        List<TraceAnchorInfo> sortTraceAnchorInfoList = userInfoList.stream().map(s -> JSON.parseObject(s, TraceAnchorInfo.class)).sorted(Comparator.comparing(TraceAnchorInfo::getLogTimeStamp)).collect(Collectors.toList());
        return buildUserTimeLineVO(sortTraceAnchorInfoList);
    }

    private UserTimeLineVO buildUserTimeLineVO(List<TraceAnchorInfo> traceAnchorInfoList) {
        /*
        针对businessId进行分类
         */
        Map<String, List<TraceAnchorInfo>> map = new HashMap<>();
        for (TraceAnchorInfo traceAnchorInfo: traceAnchorInfoList) {
            List<TraceAnchorInfo> tempTraceAnchorInfoList = map.get(String.valueOf(traceAnchorInfo.getBusinessId()));
            if (Objects.isNull(tempTraceAnchorInfoList) || tempTraceAnchorInfoList.isEmpty()) {
                tempTraceAnchorInfoList = new ArrayList<>();
            }
            tempTraceAnchorInfoList.add(traceAnchorInfo);
            map.put(String.valueOf(traceAnchorInfo.getBusinessId()), tempTraceAnchorInfoList);
        }
        /*
        封装VO
         */
        List<UserTimeLineVO.ItemsVO> items = new ArrayList<>();
        for (Map.Entry<String, List<TraceAnchorInfo>> entry: map.entrySet()) {
            Long messageTemplateId = TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.valueOf(entry.getKey()));
            MessageTemplate messageTemplate = messageTemplateMapper.findById(messageTemplateId);
            if (Objects.isNull(messageTemplate)) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            for (TraceAnchorInfo traceAnchorInfo: entry.getValue()) {
                if (traceAnchorInfo.getStatus().equals(AnchorStateEnum.RECEIVE.getCode())) {
                    sb.append(CommonConstant.CRLF);
                }
                String startTime = DateUtil.format(new Date(traceAnchorInfo.getLogTimeStamp()), DatePattern.NORM_DATETIME_FORMAT);
                String stateDescription = EnumUtils.getEnumByCode(traceAnchorInfo.getStatus(), AnchorStateEnum.class).getDescription();
                sb.append(startTime).append(CommonConstant.COLON).append(stateDescription).append(CommonConstant.ARROW);
            }
            for (String detail: sb.toString().split(CommonConstant.CRLF)) {
                if (!detail.isBlank()) {
                    UserTimeLineVO.ItemsVO itemsVO = UserTimeLineVO.ItemsVO.builder()
                            .detail(detail)
                            .businessId(entry.getKey())
                            .sendType(EnumUtils.getEnumByCode(messageTemplate.getSendChannel(), ChannelTypeEnum.class).getDescription())
                            .creator(messageTemplate.getCreator())
                            .title(messageTemplate.getName()).build();
                    items.add(itemsVO);
                }
            }
        }
        return UserTimeLineVO.builder().items(items).build();
    }
}
