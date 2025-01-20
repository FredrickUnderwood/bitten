package com.chen.bitten.api.business.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.chen.bitten.common.domain.SendTaskModel;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.IdTypeEnum;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SendTaskInfoCheckBusiness implements BusinessProcess<SendTaskModel> {

    private static final String LOG_PREFIX = "[SendTaskInfoCheckBusiness]";

    private static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";
    private static final String EMAIL_REGEX_EXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    protected static final Map<Integer, String> CHANNEL_REGEX_EXP_MAP;

    static {
        Map<Integer, String> tmpMap = new HashMap<>();
        tmpMap.put(IdTypeEnum.PHONE.getCode(), PHONE_REGEX_EXP);
        tmpMap.put(IdTypeEnum.EMAIL.getCode(), EMAIL_REGEX_EXP);
        // 初始化为不可变集合，避免被恶意修改
        CHANNEL_REGEX_EXP_MAP = tmpMap;
    }


    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        List<TaskInfo> taskInfoList = context.getProcessModel().getTaskInfoList();
        filter(taskInfoList);
        if (taskInfoList.isEmpty()) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS, "手机号或邮箱不合法，无有效发送任务"));
        }
    }

    private void filter(List<TaskInfo> taskInfoList) {
        String regexExp = CHANNEL_REGEX_EXP_MAP.get(CollUtil.getFirst(taskInfoList.iterator()).getIdType());
        Iterator<TaskInfo> taskInfoIterator = taskInfoList.iterator();
        while (taskInfoIterator.hasNext()) {
            TaskInfo taskInfo = taskInfoIterator.next();
            Set<String> illegalReceivers = taskInfo.getReceiver().stream()
                    .filter(receiver -> !ReUtil.isMatch(regexExp, receiver)).collect(Collectors.toSet());
            if (!illegalReceivers.isEmpty()) {
                taskInfo.getReceiver().removeAll(illegalReceivers);
                log.info("{}Illegal receivers: {}", LOG_PREFIX, illegalReceivers);
            }
            if (taskInfo.getReceiver().isEmpty()) {
                taskInfoIterator.remove();
            }
        }
    }
}
