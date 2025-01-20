package com.chen.bitten.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.chen.bitten.common.constant.CommonConstant;

import java.util.Date;

public class TaskInfoUtils {

    private static final long TYPE_FLAG = 1000000L;
    private static final String CODE = "track_code_bid";

    private TaskInfoUtils() {

    }

    public static String generateMessageId() {
        return IdUtil.nanoId();
    }

    public static Long generateBusinessId(Long templateId, Integer templateType) {
        Integer today = Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT));
        return Long.valueOf(String.format("%d%s", templateType.longValue() * TYPE_FLAG + templateId, today));
    }

    public static Long getMessageTemplateIdFromBusinessId (Long businessId) {
        return Long.valueOf(String.valueOf(businessId).substring(1, 8));
    }

    public static Long getDateFromBusinessId(Long businessId) {
        return Long.valueOf(String.valueOf(businessId).substring(8));
    }

    /**
     * url用于数据追踪
     * @param url
     * @param templateId
     * @param templateType
     * @return
     */
    public static String generateUrl(String url, Long templateId, Integer templateType) {
        url = url.trim();
        Long businessId = generateBusinessId(templateId, templateType);
        if (!url.contains(CommonConstant.QM)) {
            /*
            不存在问号，则开始添加条件
             */
            return url + CommonConstant.QM + CODE + CommonConstant.EQUAL + businessId;
        } else {
            /*
            存在问号，则追加条件
             */
            return url + CommonConstant.AND + CODE + CommonConstant.EQUAL + businessId;
        }
    }
}
