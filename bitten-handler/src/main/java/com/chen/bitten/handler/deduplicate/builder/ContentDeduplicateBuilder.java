package com.chen.bitten.handler.deduplicate.builder;

import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.AnchorStateEnum;
import com.chen.bitten.common.enums.DeduplicateTypeEnum;
import com.chen.bitten.handler.deduplicate.DeduplicateParam;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Component
public class ContentDeduplicateBuilder extends AbstractDeduplicateBuilder {

    public ContentDeduplicateBuilder() {
        this.DEDUPLICATE_TYPE = DeduplicateTypeEnum.CONTENT.getCode();
    }
    @Override
    public DeduplicateParam build(String deduplicateConfig, TaskInfo taskInfo) {
        DeduplicateParam deduplicateParam = getParamFromConfig(DEDUPLICATE_TYPE, deduplicateConfig, taskInfo);
        if (Objects.isNull(deduplicateParam)) {
            return null;
        }
        deduplicateParam.setAnchorState(AnchorStateEnum.CONTENT_DEDUPLICATION);
        return deduplicateParam;
    }
}
