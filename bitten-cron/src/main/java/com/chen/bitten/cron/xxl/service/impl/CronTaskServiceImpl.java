package com.chen.bitten.cron.xxl.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.cron.xxl.constant.XxlJobConstant;
import com.chen.bitten.cron.xxl.service.CronTaskService;
import com.chen.bitten.cron.xxl.domain.XxlJobGroup;
import com.chen.bitten.cron.xxl.domain.XxlJobInfo;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CronTaskServiceImpl implements CronTaskService {

    private static final String LOG_PREFIX = "[CronTaskServiceImpl]";

    @Value("${xxl.job.admin.username}")
    private String username;

    @Value("${xxl.job.admin.password}")
    private String password;

    @Value("${xxl.job.admin.address}")
    private String adminAddress;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobInfo), new TypeReference<Map<String, Object>>() {});
        String path = Objects.isNull(xxlJobInfo.getId()) ? adminAddress + XxlJobConstant.XXL_JOB_INSERT_URL : adminAddress + XxlJobConstant.XXL_JOB_UPDATE_URL;
        ReturnT returnT = null;
        HttpResponse response;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                /*
                插入时返回taskId，更新时不反悔
                 */
                if (path.contains(XxlJobConstant.XXL_JOB_INSERT_URL)) {
                    Integer taskId = Integer.parseInt(returnT.getContent().toString());
                    return BasicResultVO.success(taskId);
                } else if (path.contains(XxlJobConstant.XXL_JOB_UPDATE_URL)) {
                    return BasicResultVO.success();
                }
            }
        } catch (Exception e) {
            log.error("{}saveCronTask fail! e: {}, param: {}, response: {}", LOG_PREFIX, e.getStackTrace(),
                    JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        deleteCachedCookie();
        return BasicResultVO.fail(RespStatusEnum.SERVER_ERROR, JSON.toJSONString(returnT));

    }

    @Override
    public BasicResultVO deleteCronTask(Integer taskId) {
        String path = adminAddress + XxlJobConstant.XXL_JOB_REMOVE_URL;
        Map<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        ReturnT returnT = null;
        HttpResponse response;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVO.success();
            }

        } catch (Exception e) {
            log.error("{}deleteCronTask fail! e: {}, param: {}, response: {}", LOG_PREFIX, e.getStackTrace(),
                    JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        deleteCachedCookie();
        return BasicResultVO.fail(RespStatusEnum.SERVER_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResultVO startCronTask(Integer taskId) {
        String path = adminAddress + XxlJobConstant.XXL_JOB_START_URL;
        Map<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        ReturnT returnT = null;
        HttpResponse response;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVO.success();
            }

        } catch (Exception e) {
            log.error("{}startCronTask fail! e: {}, param: {}, response: {}", LOG_PREFIX, e.getStackTrace(),
                    JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        deleteCachedCookie();
        return BasicResultVO.fail(RespStatusEnum.SERVER_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResultVO stopCronTask(Integer taskId) {
        String path = adminAddress + XxlJobConstant.XXL_JOB_STOP_URL;
        Map<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        ReturnT returnT = null;
        HttpResponse response;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVO.success();
            }

        } catch (Exception e) {
            log.error("{}stopCronTask fail! e: {}, param: {}, response: {}", LOG_PREFIX, e.getStackTrace(),
                    JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        deleteCachedCookie();
        return BasicResultVO.fail(RespStatusEnum.SERVER_ERROR, JSON.toJSONString(returnT));
    }

    @Override
    public BasicResultVO getGroupId(String appname, String title) {
        String path = adminAddress + XxlJobConstant.XXL_GROUP_PAGE_LIST_URL;
        Map<String, Object> params = new HashMap<>();
        params.put("appname", appname);
        params.put("title", title);
        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            if (Objects.isNull(response)) {
                return BasicResultVO.fail(RespStatusEnum.SERVER_ERROR);
            }
            Integer id = JSON.parseObject(response.body()).getJSONArray("data").getJSONObject(0).getInteger("id");
            return BasicResultVO.success(id);
        } catch (Exception e) {
            log.error("{}getGroupId fail! e: {}, param: {}, response: {}", LOG_PREFIX, e.getStackTrace(),
                    JSON.toJSONString(params), response != null ? JSON.toJSONString(response.body()) : "");
        }
        deleteCachedCookie();
        return BasicResultVO.fail(RespStatusEnum.SERVER_ERROR, response != null ? JSON.toJSONString(response.body()) : "");
    }

    @Override
    public BasicResultVO createGroup(XxlJobGroup xxlJobGroup) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobGroup), new TypeReference<Map<String, Object>>() {});
        String path = adminAddress + XxlJobConstant.XXL_GROUP_INSERT_URL;
        HttpResponse response;
        ReturnT returnT = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            returnT = JSON.parseObject(response.body(), ReturnT.class);
            if (response.isOk() && ReturnT.SUCCESS_CODE == returnT.getCode()) {
                return BasicResultVO.success();
            }
        } catch (Exception e) {
            log.error("{}createGroup fail! e: {}, param: {}, response: {}", LOG_PREFIX, e.getStackTrace(),
                    JSON.toJSONString(params), JSON.toJSONString(returnT));
        }
        deleteCachedCookie();
        return BasicResultVO.fail(RespStatusEnum.SERVER_ERROR, JSON.toJSONString(returnT));
    }

    /**
     * 获取xxl的cookie
     * @return
     */
    private String getCookie() {
        String cachedCookie = (String) redisTemplate.opsForValue().get(XxlJobConstant.XXL_COOKIE_REDIS_KEY_PREFIX + username);
        if (Objects.nonNull(cachedCookie) && !cachedCookie.isBlank()) {
            return cachedCookie;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userName", username);
        params.put("password", password);

        String path = adminAddress + XxlJobConstant.XXL_LOGIN_URL;
        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).execute();
            if (response.isOk()) {
                List<HttpCookie> cookies = response.getCookies();
                StringBuilder sb = new StringBuilder();
                for (HttpCookie cookie: cookies) {
                    sb.append(cookie.toString());
                }
                redisTemplate.opsForValue().set(XxlJobConstant.XXL_COOKIE_REDIS_KEY_PREFIX + username, sb.toString());
                return sb.toString();
            }
        } catch (Exception e) {
            log.error("{}getCookie fail! e: {}, param: {}, response: {}", LOG_PREFIX, e.getStackTrace(),
                    JSON.toJSONString(params), JSON.toJSONString(response));
        }
        return null;
    }

    private void deleteCachedCookie() {
        redisTemplate.delete(XxlJobConstant.XXL_COOKIE_REDIS_KEY_PREFIX + username);
    }
}
