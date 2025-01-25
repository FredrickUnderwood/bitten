package com.chen.bitten.server.service;

import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.dto.MessageTemplateParam;
import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.common.vo.MessageTemplatePageQueryVO;

import java.util.List;

public interface MessageTemplateService {

    MessageTemplate saveOrUpdateMessageTemplate(MessageTemplate messageTemplate);

    MessageTemplatePageQueryVO queryList(MessageTemplateParam messageTemplateParam);

    MessageTemplate queryById(Long id);

    void copyById(Long id);

    void deleteByIds(List<Long> ids);

    BasicResultVO startCronTask(Long id);

    BasicResultVO stopCronTask(Long id);
}
