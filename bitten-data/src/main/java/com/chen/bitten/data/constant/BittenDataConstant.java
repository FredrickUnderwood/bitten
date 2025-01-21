package com.chen.bitten.data.constant;

public class BittenDataConstant {


    /*
    Redis配置
     */
    public static final String HOST = "47.109.43.173";
    public static final Integer PORT = 6379;
    public static final Integer DATABASE = 10;

    /*
    Kafka配置
     */
    public static final String TOPIC = "bittenBusinessLog";
    public static final String GROUP_ID = "bittenBusinessLogGroup";
    public static final String BROKER = "47.109.43.173:9092";

    /*
    Flink配置
     */
    public static final String SOURCE_NAME = "bitten_kafka_source";
    public static final String FUNCTION_NAME = "bitten_transfer";
    public static final String SINK_NAME = "bitten_sink";
    public static final String JOB_NAME = "BittenDataApplication";
}
