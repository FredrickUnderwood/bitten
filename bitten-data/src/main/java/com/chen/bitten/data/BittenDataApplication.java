package com.chen.bitten.data;

import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.data.constant.BittenDataConstant;
import com.chen.bitten.data.function.BittenFlatMapFunction;
import com.chen.bitten.data.sink.BittenSink;
import com.chen.bitten.data.utils.KafkaUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class BittenDataApplication {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> kafkaConsumer = KafkaUtils.getKafkaConsumer(BittenDataConstant.TOPIC, BittenDataConstant.GROUP_ID, BittenDataConstant.GROUP_ID);
        DataStreamSource<String> kafkaSource = env.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), BittenDataConstant.SOURCE_NAME);
        SingleOutputStreamOperator<AnchorInfo> dataStream = kafkaSource.flatMap(new BittenFlatMapFunction()).name(BittenDataConstant.FUNCTION_NAME);
        dataStream.addSink(new BittenSink()).name(BittenDataConstant.SINK_NAME);
        env.execute(BittenDataConstant.JOB_NAME);
    }
}
