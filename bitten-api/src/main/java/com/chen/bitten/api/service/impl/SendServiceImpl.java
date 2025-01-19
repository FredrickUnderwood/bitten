package com.chen.bitten.api.service.impl;

import com.chen.bitten.api.service.SendService;
import com.chen.bitten.common.domain.ResponseTaskInfo;
import com.chen.bitten.common.domain.SendTaskModel;
import com.chen.bitten.common.dto.BatchSendRequest;
import com.chen.bitten.common.dto.SendRequest;
import com.chen.bitten.common.dto.SendResponse;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.process.ProcessController;
import com.chen.bitten.common.utils.LogUtils;
import com.chen.bitten.common.vo.BasicResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 相当于handler模块的Task
 */
@Service
public class SendServiceImpl implements SendService {

    @Autowired
    private LogUtils logUtils;

    @Autowired
    @Qualifier("apiProcessController")
    private ProcessController processController;

    @Override
    public SendResponse send(SendRequest sendRequest) {
        if (Objects.isNull(sendRequest)) {
            return new SendResponse(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg(), null);
        }
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Collections.singletonList(sendRequest.getMessageParam())).build();
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .response(BasicResultVO.success())
                .needBreak(false).build();
        ProcessContext contextAfterProcess = processController.process(context);
        logUtils.print("SendService#send", String.valueOf(sendRequest.getMessageTemplateId()), sendRequest.toString());
        return new SendResponse(contextAfterProcess.getResponse().getStatus(),
                contextAfterProcess.getResponse().getMsg(),
                (List<ResponseTaskInfo>) contextAfterProcess.getResponse().getData());
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        if (Objects.isNull(batchSendRequest)) {
            return new SendResponse(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg(), null);
        }
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(batchSendRequest.getMessageTemplateId())
                .messageParamList(batchSendRequest.getMessageParamList()).build();

        ProcessContext context = ProcessContext.builder().
                code(batchSendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();
        ProcessContext contextAfterProcess = processController.process(context);
        logUtils.print("SendService#batchSend", String.valueOf(batchSendRequest.getMessageTemplateId()), batchSendRequest.toString());
        return new SendResponse(contextAfterProcess.getResponse().getStatus(),
                contextAfterProcess.getResponse().getMsg(),
                (List<ResponseTaskInfo>) contextAfterProcess.getResponse().getData());
    }
}
