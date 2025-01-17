package com.chen.bitten.handler.deduplicate.service;

import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.handler.deduplicate.DeduplicateHolder;
import com.chen.bitten.handler.deduplicate.DeduplicateParam;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class AbstractDeduplicateService implements DeduplicateService {
    protected Integer DEDUPLICATE_TYPE;

    @Autowired
    private DeduplicateHolder deduplicateHolder;

    @PostConstruct
    public void init() {
        deduplicateHolder.putDeduplicateService(DEDUPLICATE_TYPE, this);
    }
}
