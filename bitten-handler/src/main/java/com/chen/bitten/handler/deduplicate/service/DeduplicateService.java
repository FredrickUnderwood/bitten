package com.chen.bitten.handler.deduplicate.service;

import com.chen.bitten.handler.deduplicate.DeduplicateParam;

/**
 * 执行的duplicateParam
 */
public interface DeduplicateService {

    void deduplicate(DeduplicateParam deduplicateParam);
}
