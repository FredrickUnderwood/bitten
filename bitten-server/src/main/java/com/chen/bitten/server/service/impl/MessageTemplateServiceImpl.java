package com.chen.bitten.server.service.impl;

import cn.hutool.core.date.DateUtil;
import com.chen.bitten.common.constant.BittenConstant;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.dto.MessageTemplateParam;
import com.chen.bitten.common.enums.AuditStatus;
import com.chen.bitten.common.enums.MessageStatus;
import com.chen.bitten.common.enums.TemplateType;
import com.chen.bitten.common.mapper.MessageTemplateMapper;
import com.chen.bitten.common.vo.MessageTemplatePageQueryVO;
import com.chen.bitten.cron.xxl.domain.XxlJobInfo;
import com.chen.bitten.cron.xxl.service.CronTaskService;
import com.chen.bitten.cron.xxl.utils.XxlJobUtils;
import com.chen.bitten.server.service.MessageTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        PageHelper.startPage(messageTemplateParam.getPage(), messageTemplateParam.getPageSize());
        Page<MessageTemplate> messageTemplates = messageTemplateMapper.queryPage(messageTemplateParam);
        return MessageTemplatePageQueryVO.builder().rows(messageTemplates).total(messageTemplates.getTotal()).build();
    }

    @Override
    public MessageTemplate queryById(Long id) {
        return null;
    }

    @Override
    public void copyById(Long id) {

    }

    @Override
    public void deleteByIds(List<Long> ids) {

    }
}
