package com.chen.bitten.handler.deduplicate.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.handler.deduplicate.DeduplicateHolder;
import com.chen.bitten.handler.deduplicate.DeduplicateParam;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

abstract public class AbstractDeduplicateBuilder implements DeduplicateBuilder {

    protected Integer DEDUPLICATE_TYPE;

    @Autowired
    private DeduplicateHolder deduplicateHolder;

    @PostConstruct
    public void init() {
        deduplicateHolder.putDeduplicateBuilder(DEDUPLICATE_TYPE, this);
    }

    public DeduplicateParam getParamFromConfig(Integer key, String deduplicateConfig, TaskInfo taskInfo) {
        JSONObject object = JSON.parseObject(deduplicateConfig);
        if (Objects.isNull(object)) {
            return null;
        }
        DeduplicateParam deduplicateParam = JSON.parseObject(object.getString(DEDUPLICATE_CONFIG_PREFIX + key), DeduplicateParam.class);
        if (Objects.isNull(deduplicateParam)) {
            return null;
        }
        deduplicateParam.setTaskInfo(taskInfo);
        return deduplicateParam;



    }
}
