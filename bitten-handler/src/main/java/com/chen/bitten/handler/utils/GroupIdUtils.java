package com.chen.bitten.handler.utils;


import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.ChannelTypeEnum;
import com.chen.bitten.common.enums.MessageTypeEnum;
import com.chen.bitten.common.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * groupId标志着一个消费者组
 */
public class GroupIdUtils {

    private GroupIdUtils() {
    }
    public static List<String> getAllGroupIds() {
        List<String> groupIds = new ArrayList<>();
        for (ChannelTypeEnum channelType: ChannelTypeEnum.values()) {
            for (MessageTypeEnum messageType: MessageTypeEnum.values()) {
                groupIds.add(channelType.getCodeEn() + "." + messageType.getCodeEn());
            }
        }
        return groupIds;
    }


    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelTypeCodeEn = EnumUtils.getEnumByCode(taskInfo.getSendChannel(), ChannelTypeEnum.class).getCodeEn();
        String messageTypeCodeEn = EnumUtils.getEnumByCode(taskInfo.getMessageType(), MessageTypeEnum.class).getCodeEn();
        return channelTypeCodeEn + "." + messageTypeCodeEn;
    }





}
