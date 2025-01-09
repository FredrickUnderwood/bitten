package com.chen.bitten.handler.service.impl;

import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.handler.handler.pending.Task;
import com.chen.bitten.handler.handler.pending.TaskPendingHolder;
import com.chen.bitten.handler.service.ConsumeService;
import com.chen.bitten.handler.utils.GroupIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    @Override
    public void consume2Send(List<TaskInfo> taskInfoList) {
        String groupId = GroupIdUtils.getGroupIdByTaskInfo(Objects.requireNonNull(taskInfoList.stream().findFirst().orElse(null)));
        for (TaskInfo taskInfo: taskInfoList) {
            // TODO 日志埋点
            Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
            taskPendingHolder.route(groupId).execute(task);
        }
    }

    // TODO 撤回逻辑
}
