package com.chen.bitten.handler.service.impl;

import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.ObjectInfo;
import com.chen.bitten.common.domain.RecallTaskInfo;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.AnchorStateEnum;
import com.chen.bitten.common.utils.LogUtils;
import com.chen.bitten.handler.handler.HandlerHolder;
import com.chen.bitten.handler.handler.pending.Task;
import com.chen.bitten.handler.handler.pending.TaskPendingHolder;
import com.chen.bitten.handler.service.ConsumeService;
import com.chen.bitten.handler.utils.GroupIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ConsumeServiceImpl implements ConsumeService {

    private static final String BIZ_TYPE = "Receiver#consumer";
    private static final String RECALL_BIZ_TYPE = "Receiver#recall";

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    @Autowired
    private LogUtils logUtils;
    @Autowired
    private HandlerHolder handlerHolder;

    @Override
    public void consume2Send(List<TaskInfo> taskInfoList) {
        String groupId = GroupIdUtils.getGroupIdByTaskInfo(Objects.requireNonNull(taskInfoList.stream().findFirst().orElse(null)));
        for (TaskInfo taskInfo: taskInfoList) {
            logUtils.print(ObjectInfo.builder().object(taskInfo).bizType(BIZ_TYPE).build(), AnchorInfo.builder()
                    .bizId(taskInfo.getBizId()).businessId(taskInfo.getBusinessId())
                    .messageId(taskInfo.getMessageId()).state(AnchorStateEnum.RECEIVE.getCode())
                    .receiver(taskInfo.getReceiver()).build());
            Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
            taskPendingHolder.route(groupId).execute(task);
        }
    }

    @Override
    public void consume2Recall(RecallTaskInfo recallTaskInfo) {
        logUtils.print(ObjectInfo.builder()
                .bizType(RECALL_BIZ_TYPE)
                .object(recallTaskInfo).build());
        handlerHolder.route(recallTaskInfo.getSendChannel()).recall(recallTaskInfo);
    }


}
