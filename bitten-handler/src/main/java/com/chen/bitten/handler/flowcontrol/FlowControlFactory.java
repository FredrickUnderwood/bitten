package com.chen.bitten.handler.flowcontrol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chen.bitten.common.config.ConfigCenter;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.ChannelTypeEnum;
import com.chen.bitten.common.enums.RateLimitStrategy;
import com.chen.bitten.common.utils.EnumUtils;
import com.chen.bitten.handler.flowcontrol.annotations.RateLimit;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class FlowControlFactory implements ApplicationContextAware {

    private static final String FLOW_CONTROL_KEY = "flowControlRule";
    private static final String FLOW_CONTROL_RULE_PREFIX = "flow_control_";

    private static final String LOG_PREFIX = "FlowControlFactory";

    private final Map<RateLimitStrategy, FlowControlService> flowControlServiceMap = new ConcurrentHashMap<>();

    @Autowired
    private ConfigCenter configCenter;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
        RateLimiter rateLimiter;
        Double rateLimitInitValue = flowControlParam.getRateLimitInitValue();
        Double rateLimitConfigValue = getRateLimitConfigValue(taskInfo.getSendChannel());
        if (Objects.nonNull(rateLimitConfigValue) && !rateLimitConfigValue.equals(rateLimitInitValue)) {
            rateLimiter = RateLimiter.create(rateLimitConfigValue);
            flowControlParam.setRateLimiter(rateLimiter);
            flowControlParam.setRateLimitInitValue(rateLimitConfigValue);
        }
        FlowControlService flowControlService = flowControlServiceMap.get(flowControlParam.getRateLimitStrategy());
        if (Objects.isNull(flowControlService)) {
            log.error("{}Flow control service not found", LOG_PREFIX);
            return;
        }
        double costTime = flowControlService.flowControl(taskInfo, flowControlParam);
        if (costTime > 0) {
            log.info("{}Send channel: {} flow control cost time: {}", LOG_PREFIX,
                    EnumUtils.getEnumByCode(taskInfo.getSendChannel(), ChannelTypeEnum.class).getDescription(), costTime);
        }
    }
    
    private Double getRateLimitConfigValue(Integer sendChannel) {
        String flowControlConfig = configCenter.getProperty(FLOW_CONTROL_KEY, CommonConstant.EMPTY_JSON_OBJECT);
        JSONObject jsonObject = JSON.parseObject(flowControlConfig);
        String key = FLOW_CONTROL_RULE_PREFIX + sendChannel;
        if (Objects.isNull(jsonObject.getDouble(key))) {
            return null;
        }
        return jsonObject.getDouble(key);
    }

    @PostConstruct
    public void init() {
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RateLimit.class);
        serviceMap.forEach((name, service) -> {
            RateLimit rateLimit = service.getClass().getAnnotation(RateLimit.class);
            RateLimitStrategy rateLimitStrategy = rateLimit.rateLimitStrategy();
            flowControlServiceMap.put(rateLimitStrategy, (FlowControlService) service);
        });
    }
}
