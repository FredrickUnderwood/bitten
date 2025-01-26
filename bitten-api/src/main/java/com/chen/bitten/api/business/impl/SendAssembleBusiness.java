package com.chen.bitten.api.business.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.MessageParam;
import com.chen.bitten.common.domain.SendTaskModel;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.dto.model.ContentModel;
import com.chen.bitten.common.enums.ChannelTypeEnum;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.mapper.MessageTemplateMapper;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.utils.ContentHolderUtils;
import com.chen.bitten.common.utils.TaskInfoUtils;
import com.chen.bitten.common.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 组装参数
 */
@Slf4j
@Service
public class SendAssembleBusiness implements BusinessProcess<SendTaskModel> {

    private static final String LOG_PREFIX = "[SendAssembleBusiness]";

    private static final String LINK_FIELD_NAME = "url";

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        try {
            MessageTemplate messageTemplate = messageTemplateMapper.findById(messageTemplateId);
            if (Objects.isNull(messageTemplate) || messageTemplate.getIsDeleted().equals(1)) {
                context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.MESSAGE_TEMPLATE_NOT_FOUND));
                return;
            }
            List<TaskInfo> taskInfoList = assembleTaskInfoList(sendTaskModel, messageTemplate);
            sendTaskModel.setTaskInfoList(taskInfoList);
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVER_ERROR));
            log.error("{}process fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
    }

    /**
     * 获取ContentModel，替换messageContent中的占位符
     */
    private ContentModel getContentModelValue(MessageTemplate messageTemplate, MessageParam messageParam) {
        Integer sendChannel = messageTemplate.getSendChannel();
        Class <? extends ContentModel> contentModelClass = ChannelTypeEnum.getContentModelClassByCode(sendChannel);

        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMessageContent());

        /*
        组装具体的ContentModel
        每个field都包含数个需要替换的内容
         */
        Field[] fields = ReflectUtil.getFields(contentModelClass);
        ContentModel contentModel = ReflectUtil.newInstance(contentModelClass);
        for (Field field: fields) {
            String originValue = jsonObject.getString(field.getName());
            if (!originValue.isBlank()) {
                /*
                填充占位符
                 */
                String updatedValue = ContentHolderUtils.replacePlaceholder(originValue, variables);
                Object updatedObject = JSONUtil.isTypeJSONObject(updatedValue) ? JSONUtil.toBean(updatedValue, field.getType()) : updatedValue;
                ReflectUtil.setFieldValue(contentModel, field, updatedObject);
            }
        }
        String url = (String) ReflectUtil.getFieldValue(contentModel, LINK_FIELD_NAME);
        if (Objects.nonNull(url) && !url.isBlank()) {
            String updatedUrl = TaskInfoUtils.generateUrl(url, messageTemplate.getId(), messageTemplate.getTemplateType());
            ReflectUtil.setFieldValue(contentModel, LINK_FIELD_NAME, updatedUrl);
        }
        return contentModel;
    }

    public List<TaskInfo> assembleTaskInfoList(SendTaskModel sendTaskModel, MessageTemplate messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();
        for (MessageParam messageParam: messageParamList) {
            TaskInfo taskInfo = TaskInfo.builder()
                    .messageId(TaskInfoUtils.generateMessageId())
                    .bizId(messageParam.getBizId())
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceivers().split(CommonConstant.COMMA))))
                    .idType(messageTemplate.getIdType())
                    .templateType(messageTemplate.getTemplateType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .messageType(messageTemplate.getMessageType())
                    .shieldType(messageTemplate.getShieldType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam)).build();
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }
}
