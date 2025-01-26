package com.chen.bitten.common.constant;

/**
 * 线程池初始化需要的常量信息
 */
public class ThreadPoolConstant {
    public static final Integer COMMON_CORE_POOL_SIZE = 2;
    public static final Integer COMMON_MAXIMUM_POOL_SIZE = 2;
    public static final Long COMMON_KEEP_ALIVE_TIME = 10L;
    public static final Integer COMMON_QUEUE_CAPACITY = 128;

    public static final Integer SINGLE_CORE_POOL_SIZE = 1;
    public static final Integer SINGLE_MAXIMUM_POOL_SIZE = 1;
    public static final Integer SINGLE_KEEP_ALIVE_TIME = 10;

    public static final Integer BLOCKING_QUEUE_SIZE = 1024;

    public static final Long TIME_THRESHOLD = 1000L;
    public static final Integer BATCH_THRESHOLD = 100;
}
