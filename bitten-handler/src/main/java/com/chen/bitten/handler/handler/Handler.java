package com.chen.bitten.handler.handler;

import com.chen.bitten.common.domain.RecallTaskInfo;
import com.chen.bitten.common.domain.TaskInfo;

/**
 * 消息处理器接口
 */
public interface Handler {

    void doHandler(TaskInfo taskInfo);

    void recall(RecallTaskInfo recallTaskInfo);

}
