package com.chen.bitten.handler.config;

import com.chen.bitten.common.process.ProcessController;
import com.chen.bitten.common.process.ProcessTemplate;
import com.chen.bitten.handler.business.impl.DeduplicateBusiness;
import com.chen.bitten.handler.business.impl.DiscardBusiness;
import com.chen.bitten.handler.business.impl.SendMessageBusiness;
import com.chen.bitten.handler.business.impl.ShieldBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler层 流程（包括sendMessage等）配置
 */
@Configuration
public class HandlerProcessConfig {

    public static final String HANDLER_PROCESS_CODE = "handler";

    @Autowired
    private DiscardBusiness discardBusiness;

    @Autowired
    private ShieldBusiness shieldBusiness;

    @Autowired
    private DeduplicateBusiness deduplicateBusiness;

    @Autowired
    private SendMessageBusiness sendMessageBusiness;

    @Bean("handlerProcessTemplate")
    public ProcessTemplate processTemplate() {
        ProcessTemplate template = new ProcessTemplate();
        // TODO 把具体的BusinessProcess(Business)加入到template中
        template.setProcessList(Arrays.asList(discardBusiness, shieldBusiness, deduplicateBusiness, sendMessageBusiness));
        return template;
    }

    @Bean("handlerProcessController")
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templates = new HashMap<>(4);
        templates.put(HANDLER_PROCESS_CODE, processTemplate());
        processController.setTemplates(templates);
        return processController;
    }


}
