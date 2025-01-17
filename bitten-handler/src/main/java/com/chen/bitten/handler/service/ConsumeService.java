package com.chen.bitten.handler.service;

import com.chen.bitten.common.domain.TaskInfo;

import java.util.List;

public interface ConsumeService {

    void consume2Send(List<TaskInfo> taskInfoList);

    // TODO 撤回接口
}
