package com.chen.bitten.common.config;

import cn.hutool.setting.dialect.Props;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ConfigCenter {

    private static final String LOCAL_PROPERTIES_PATH = "local.yml";
    private final Props PROPS = new Props(LOCAL_PROPERTIES_PATH, StandardCharsets.UTF_8);

    @Value("${bitten.apollo.enabled}")
    private Boolean enableApollo;
    @Value("${bitten.nacos.enabled}")
    private Boolean enableNacos;
    /**
     * 优先查远程配置
     * 远程配置不存在，查本地配置
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        if (enableApollo) {
            return null;
        } else if (enableNacos) {
            return null;
        } else {
            return PROPS.getProperty(key, defaultValue);
        }

    }
}
