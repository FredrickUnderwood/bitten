package com.chen.bitten.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.constant.BittenConstant;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.dto.MessageTemplateParam;
import com.chen.bitten.common.enums.AuditStatus;
import com.chen.bitten.common.enums.MessageStatus;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.enums.TemplateType;
import com.chen.bitten.common.mapper.MessageTemplateMapper;
import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.common.vo.MessageTemplatePageQueryVO;
import com.chen.bitten.cron.xxl.domain.XxlJobInfo;
import com.chen.bitten.cron.xxl.service.CronTaskService;
import com.chen.bitten.cron.xxl.utils.XxlJobUtils;
import com.chen.bitten.server.service.MessageTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    private CronTaskService cronTaskService;

    @Autowired
    private XxlJobUtils xxlJobUtils;

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public MessageTemplate saveOrUpdateMessageTemplate(MessageTemplate messageTemplate) {
        messageTemplate.setUpdatedTime(Math.toIntExact(DateUtil.currentSeconds()));
        if (Objects.isNull(messageTemplate.getId())) {
            initMessageTemplateStatus(messageTemplate);
            Long id = messageTemplateMapper.insert(messageTemplate);
            messageTemplate.setId(id);
        } else {
            resetMessageTemplateStatus(messageTemplate);
            messageTemplateMapper.update(messageTemplate);
        }
        return messageTemplate;
    }

    private void initMessageTemplateStatus(MessageTemplate messageTemplate) {
        messageTemplate.setFlowId(CommonConstant.EMPTY_STRING)
                .setMessageStatus(MessageStatus.INIT.getCode())
                .setAuditStatus(AuditStatus.WAIT_AUDIT.getCode())
                .setCreator(messageTemplate.getCreator().isBlank() ? BittenConstant.DEFAULT_CREATOR : messageTemplate.getCreator())
                .setAuditor(messageTemplate.getAuditor().isBlank() ? BittenConstant.DEFAULT_AUDITOR : messageTemplate.getAuditor())
                .setUpdater(messageTemplate.getUpdater().isBlank() ? BittenConstant.DEFAULT_UPDATER : messageTemplate.getUpdater())
                .setTeam(messageTemplate.getTeam().isBlank() ? BittenConstant.DEFAULT_TEAM : messageTemplate.getTeam())
                .setCreatedTime(Math.toIntExact(DateUtil.currentSeconds()))
                .setIsDeleted(0);
    }

    private void resetMessageTemplateStatus(MessageTemplate messageTemplate) {
        messageTemplate.setMessageStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode());
        if (messageTemplate.getTemplateType().equals(TemplateType.REALTIME.getCode())) {
            return;
        }
        // 从数据库查CronTaskId
        MessageTemplate oldMessageTemplate = messageTemplateMapper.findById(messageTemplate.getId());
        if (Objects.nonNull(oldMessageTemplate) && Objects.nonNull(oldMessageTemplate.getCronTaskId())) {
            messageTemplate.setCronTaskId(oldMessageTemplate.getCronTaskId());
        }
        if (Objects.nonNull(messageTemplate.getCronTaskId()) && messageTemplate.getTemplateType().equals(TemplateType.CLOCKING.getCode())) {
            XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplate);
            cronTaskService.saveCronTask(xxlJobInfo);
            cronTaskService.stopCronTask(messageTemplate.getCronTaskId());
        }
    }



    @Override
    public MessageTemplatePageQueryVO queryList(MessageTemplateParam messageTemplateParam) {
        String creator = messageTemplateParam.getCreator().isBlank() ? BittenConstant.DEFAULT_CREATOR : messageTemplateParam.getCreator();
        messageTemplateParam.setCreator(creator);
        PageHelper.startPage(messageTemplateParam.getPage(), messageTemplateParam.getPerPage());
        Page<MessageTemplate> messageTemplates = messageTemplateMapper.queryPage(messageTemplateParam);
        return MessageTemplatePageQueryVO.builder().rows(messageTemplates).total(messageTemplates.getTotal()).build();
    }

    @Override
    public MessageTemplate queryById(Long id) {
        return messageTemplateMapper.findById(id);
    }

    @Override
    @Transactional
    public void copyById(Long id) {
        MessageTemplate messageTemplate = messageTemplateMapper.findById(id);
        if (Objects.nonNull(messageTemplate)) {
            MessageTemplate clone = ObjectUtil.clone(messageTemplate).setId(null).setCronTaskId(null);
            messageTemplateMapper.insert(clone);
        }
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        List<MessageTemplate> messageTemplates = messageTemplateMapper.findByIds(ids);
        for (MessageTemplate messageTemplate: messageTemplates) {
            messageTemplate.setIsDeleted(1);
            if (Objects.nonNull(messageTemplate.getCronTaskId()) && messageTemplate.getCronTaskId() > 0) {
                cronTaskService.deleteCronTask(messageTemplate.getCronTaskId());
            }
            messageTemplateMapper.update(messageTemplate);
        }
    }

    @Override
    @Transactional
    public BasicResultVO startCronTask(Long id) {
        MessageTemplate messageTemplate = messageTemplateMapper.findById(id);
        if (Objects.isNull(messageTemplate)) {
            return BasicResultVO.fail();
        }

        XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplate);

        Integer taskId = messageTemplate.getCronTaskId();
        BasicResultVO basicResultVO = cronTaskService.saveCronTask(xxlJobInfo);
        if (Objects.isNull(taskId) && RespStatusEnum.SUCCESS.getCode().equals(basicResultVO.getStatus()) && Objects.nonNull(basicResultVO.getData())) {
            taskId = Integer.valueOf(String.valueOf(basicResultVO.getData()));
        }

        if (Objects.nonNull(taskId)) {
            cronTaskService.startCronTask(taskId);
            MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMessageStatus(MessageStatus.RUN.getCode())
                    .setCronTaskId(taskId).setUpdatedTime(Math.toIntExact(DateUtil.currentSeconds()));
            messageTemplateMapper.update(clone);
            return BasicResultVO.success();
        }
        return BasicResultVO.fail();
    }

    @Override
    public BasicResultVO stopCronTask(Long id) {
        MessageTemplate messageTemplate = messageTemplateMapper.findById(id);
        if (Objects.isNull(messageTemplate)) {
            return BasicResultVO.fail();
        }
        MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMessageStatus(MessageStatus.STOP.getCode())
                .setUpdatedTime(Math.toIntExact(DateUtil.currentSeconds()));
        messageTemplateMapper.update(clone);

        return cronTaskService.stopCronTask(clone.getCronTaskId());
    }
}
