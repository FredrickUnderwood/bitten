package com.chen.bitten.handler.flowcontrol;

import com.chen.bitten.common.domain.TaskInfo;

public interface FlowControlService {

    Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam);
}
