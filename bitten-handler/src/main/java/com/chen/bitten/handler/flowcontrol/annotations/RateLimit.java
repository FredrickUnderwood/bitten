package com.chen.bitten.handler.flowcontrol.annotations;

import com.chen.bitten.common.enums.RateLimitStrategy;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RateLimit {
    RateLimitStrategy rateLimitStrategy() default RateLimitStrategy.REQUEST_RATE_LIMIT;
}
