package com.chen.bitten.handler.deduplicate;

import com.chen.bitten.handler.deduplicate.builder.DeduplicateBuilder;
import com.chen.bitten.handler.deduplicate.service.DeduplicateService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeduplicateHolder {
    private final Map<Integer, DeduplicateBuilder> builderHolder = new HashMap<>(4);
    private final Map<Integer, DeduplicateService> deduplicateServiceHolder = new HashMap<>(4);

    public DeduplicateBuilder selectDeduplicateBuilder(Integer key) {
        return builderHolder.get(key);
    }
    public DeduplicateService selectDeduplicateService(Integer key) {
        return deduplicateServiceHolder.get(key);
    }
    public void putDeduplicateBuilder(Integer key, DeduplicateBuilder deduplicateBuilder) {
        builderHolder.put(key, deduplicateBuilder);
    }
    public void putDeduplicateService(Integer key, DeduplicateService deduplicateService) {
        deduplicateServiceHolder.put(key, deduplicateService);
    }
}
