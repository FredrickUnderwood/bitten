package com.chen.bitten.api.service.impl;

import com.chen.bitten.api.service.RecallService;
import com.chen.bitten.common.domain.RecallTaskModel;
import com.chen.bitten.common.dto.SendRequest;
import com.chen.bitten.common.dto.SendResponse;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.process.ProcessController;
import com.chen.bitten.common.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RecallServiceImpl implements RecallService {

    @Autowired
    private LogUtils logUtils;

    @Autowired
    @Qualifier("apiProcessController")
    private ProcessController processController;

    @Override
    public SendResponse recall(SendRequest sendRequest) {
        if (Objects.isNull(sendRequest)) {
            return new SendResponse(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg(), null);
        }
        RecallTaskModel recallTaskModel = RecallTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .recallMessageIdList(sendRequest.getRecallMessageIdList()).build();
        ProcessContext context = ProcessContext.builder().code(sendRequest.getCode())
                .processModel(recallTaskModel).needBreak(false).build();
        ProcessContext contextAfterProcess = processController.process(context);
        return new SendResponse(contextAfterProcess.getResponse().getStatus(), contextAfterProcess.getResponse().getMsg(), null);
    }
}
