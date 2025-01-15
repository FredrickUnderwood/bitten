package com.chen.bitten.handler.deduplicate.builder;

import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.handler.deduplicate.DeduplicateParam;

/**
 * 提供deduplicateParam
 */
public interface DeduplicateBuilder {

    String DEDUPLICATE_CONFIG_PREFIX = "deduplicate_";

    DeduplicateParam build(String deduplicateConfig, TaskInfo taskInfo);

}
