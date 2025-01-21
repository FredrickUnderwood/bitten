package com.chen.bitten.data.function;

import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.domain.AnchorInfo;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class BittenFlatMapFunction implements FlatMapFunction<String, AnchorInfo> {
    @Override
    public void flatMap(String value, Collector<AnchorInfo> collector) {
        AnchorInfo anchorInfo = JSON.parseObject(value, AnchorInfo.class);
        collector.collect(anchorInfo);
    }
}
