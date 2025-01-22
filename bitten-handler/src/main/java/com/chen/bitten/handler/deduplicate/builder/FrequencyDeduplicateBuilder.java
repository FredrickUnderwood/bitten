package com.chen.bitten.handler.deduplicate.builder;

import cn.hutool.core.date.DateUtil;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.AnchorStateEnum;
import com.chen.bitten.common.enums.DeduplicateTypeEnum;
import com.chen.bitten.handler.deduplicate.DeduplicateParam;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class FrequencyDeduplicateBuilder extends AbstractDeduplicateBuilder{

    public FrequencyDeduplicateBuilder() {
        this.DEDUPLICATE_TYPE = DeduplicateTypeEnum.FREQUENCY.getCode();
    }

    @Override
    public DeduplicateParam build(String deduplicateConfig, TaskInfo taskInfo) {
        DeduplicateParam deduplicateParam = getParamFromConfig(DEDUPLICATE_TYPE, deduplicateConfig, taskInfo);
        if (Objects.isNull(deduplicateParam)) {
            return null;
        }
        deduplicateParam.setDeduplicateTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicateParam.setAnchorState(AnchorStateEnum.FREQUENCY_DEDUPLICATION);
        return deduplicateParam;
    }
}
