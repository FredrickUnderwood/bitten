package com.chen.bitten.cron.xxl.constant;

public class XxlJobConstant {

    public static final String XXL_LOGIN_URL = "/login";
    public static final String XXL_JOB_INSERT_URL = "/jobinfo/add";
    public static final String XXL_JOB_UPDATE_URL = "/jobinfo/update";
    public static final String XXL_JOB_REMOVE_URL = "/jobinfo/remove";
    public static final String XXL_JOB_START_URL = "/jobinfo/start";
    public static final String XXL_JOB_STOP_URL = "/jobinfo/stop";

    public static final String XXL_GROUP_PAGE_LIST_URL = "/jobgroup/pageList";
    public static final String XXL_GROUP_INSERT_URL = "/jobgroup/save";

    public static final String XXL_COOKIE_REDIS_KEY_PREFIX = "xxl_cookie_";

    public static final Integer INSTANT_JOB_DELAY_SECONDS = 10;
    public static final Integer TIMEOUT_SECONDS = 120;
    public static final Integer RETRY_COUNT = 0;

    public static final String JOB_HANDLER_NAME = "bittenCronJob";
}
