package com.chen.bitten.handler.business.impl;

import com.chen.bitten.common.config.ConfigCenter;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.DeduplicateTypeEnum;
import com.chen.bitten.common.process.BusinessProcess;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.common.utils.EnumUtils;
import com.chen.bitten.handler.deduplicate.DeduplicateHolder;
import com.chen.bitten.handler.deduplicate.DeduplicateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DeduplicateBusiness implements BusinessProcess<TaskInfo> {

    private static final String DEDUPLICATE_RULE_KEY = "deduplicationRule";

    @Autowired
    private DeduplicateHolder deduplicateHolder;

    @Autowired
    private ConfigCenter configCenter;

    @Override
    public void process(ProcessContext<TaskInfo> context) {
        TaskInfo taskInfo = context.getProcessModel();
        String deduplicateConfig = configCenter.getProperty(DEDUPLICATE_RULE_KEY, CommonConstant.EMPTY_JSON_OBJECT);
        List<Integer> deduplicateTypeList = EnumUtils.getCodeList(DeduplicateTypeEnum.class);
        for (Integer deduplicateType: deduplicateTypeList) {
            DeduplicateParam deduplicateParam = deduplicateHolder.selectDeduplicateBuilder(deduplicateType).build(deduplicateConfig, taskInfo);
            if (Objects.nonNull(deduplicateParam)) {
                deduplicateHolder.selectDeduplicateService(deduplicateType).deduplicate(deduplicateParam);
            }
        }
        if (taskInfo.getReceiver().isEmpty()) {
            context.setNeedBreak(true);
        }
    }
}
