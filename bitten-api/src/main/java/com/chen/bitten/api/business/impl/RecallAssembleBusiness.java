package com.chen.bitten.api.business.impl;

import com.chen.bitten.common.domain.RecallTaskInfo;
import com.chen.bitten.common.domain.RecallTaskModel;
import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.mapper.MessageTemplateMapper;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RecallAssembleBusiness implements BusinessProcess<RecallTaskModel> {

    private static final String LOG_PREFIX = "[RecallAssembleBusiness]";

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;


    @Override
    public void process(ProcessContext<RecallTaskModel> context) {
        RecallTaskModel recallTaskModel = context.getProcessModel();
        try {
            MessageTemplate messageTemplate = messageTemplateMapper.findById(recallTaskModel.getMessageTemplateId());
            if (Objects.isNull(messageTemplate) || messageTemplate.getIsDeleted().equals(1)) {
                context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.MESSAGE_TEMPLATE_NOT_FOUND));
                return;
            }
            RecallTaskInfo recallTaskInfo = RecallTaskInfo.builder()
                    .messageTemplateId(recallTaskModel.getMessageTemplateId())
                    .recallMessageIdList(recallTaskModel.getRecallMessageIdList())
                    .sendAccount(messageTemplate.getSendAccount())
                    .sendChannel(messageTemplate.getSendChannel()).build();
            recallTaskModel.setRecallTaskInfo(recallTaskInfo);
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVER_ERROR));
            log.error("{}process fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }



    }
}
