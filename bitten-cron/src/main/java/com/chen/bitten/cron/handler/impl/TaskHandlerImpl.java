package com.chen.bitten.cron.handler.impl;

import com.chen.bitten.common.csv.CountCsvRowHandler;
import com.chen.bitten.common.domain.CrowdTaskInfo;
import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.mapper.MessageTemplateMapper;
import com.chen.bitten.common.utils.CsvUtils;
import com.chen.bitten.cron.handler.TaskHandler;
import com.chen.bitten.cron.pending.CrowdBatchTaskPending;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class TaskHandlerImpl implements TaskHandler {

    private static final String LOG_PREFIX = "[TaskHandlerImpl]";


    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void handle(Long messageTemplateId) {
        MessageTemplate messageTemplate = messageTemplateMapper.findById(messageTemplateId);
        if (Objects.isNull(messageTemplate)) {
            return;
        }
        if (messageTemplate.getCronCrowdPath().isBlank()) {
            log.error("{}handler fail! messageTemplateId: {} not found", LOG_PREFIX, messageTemplateId);
            return;
        }
        // 获取csv行数
        Long countCsvRow = CsvUtils.countCsvRow(messageTemplate.getCronCrowdPath(), new CountCsvRowHandler());

        // 读取文件的每一行做处理
        CrowdBatchTaskPending crowdBatchTaskPending = applicationContext.getBean(CrowdBatchTaskPending.class);
        CsvUtils.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
            if (row.getFieldMap().isEmpty() || row.getFieldMap().get(CsvUtils.RECEIVER_HEADER).isBlank()) {
                return;
            }
            // 每一行交给LazyPending处理
            Map<String, String> params = CsvUtils.getParamFromLine(row.getFieldMap());
            CrowdTaskInfo crowdTaskInfo = CrowdTaskInfo.builder()
                    .messageTemplateId(messageTemplateId)
                    .params(params)
                    .receiver(row.getFieldMap().get(CsvUtils.RECEIVER_HEADER)).build();
            crowdBatchTaskPending.pending(crowdTaskInfo);
        });

    }
}
