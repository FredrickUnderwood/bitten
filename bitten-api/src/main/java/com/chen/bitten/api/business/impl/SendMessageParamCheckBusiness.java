package com.chen.bitten.api.business.impl;

import com.chen.bitten.common.constant.BittenConstant;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.MessageParam;
import com.chen.bitten.common.domain.SendTaskModel;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.vo.BasicResultVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SendMessageParamCheckBusiness implements BusinessProcess<SendTaskModel> {

    /**
     * 检查context
     * 1.是否为空
     * 2.receiver是否为空
     * 3.receiver数量是否小于100
     * @param context
     */
    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        if (Objects.isNull(messageTemplateId) || Objects.isNull(messageParamList) || messageParamList.isEmpty()) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS, "MessageTemplateId or MessageParamList null"));
            return;
        }

        List<MessageParam> filterMessageParamList = messageParamList.stream()
                .filter(messageParam -> !messageParam.getReceivers().isBlank()).collect(Collectors.toList());
        if (filterMessageParamList.isEmpty()) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS, "Receivers null"));
            return;
        }

        if (filterMessageParamList.stream().anyMatch(messageParam -> messageParam.getReceivers().split(CommonConstant.COMMA).length > BittenConstant.MAX_BATCH_RECEIVER)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.TOO_MANY_RECEIVERS));
            return;
        }
        sendTaskModel.setMessageParamList(filterMessageParamList);


    }
}
