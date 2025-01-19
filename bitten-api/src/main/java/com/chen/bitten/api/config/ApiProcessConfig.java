package com.chen.bitten.api.config;

import com.chen.bitten.api.business.impl.SendMqBusiness;
import com.chen.bitten.common.process.ProcessController;
import com.chen.bitten.common.process.ProcessTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApiProcessConfig {

    private static final String SEND_TEMPLATE_CODE = "send";

    private static final String RECALL_TEMPLATE_CODE = "recall";

    @Autowired
    private SendMqBusiness sendMqBusiness;

    @Bean("apiSendProcessTemplate")
    public ProcessTemplate sendProcessTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(sendMqBusiness));
        return processTemplate;
    }

    @Bean("apiRecallProcessTemplate")
    public ProcessTemplate recallProcessTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        return processTemplate;
    }

    @Bean("apiProcessController")
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templates = new HashMap<>();
        templates.put(SEND_TEMPLATE_CODE, sendProcessTemplate());
        templates.put(RECALL_TEMPLATE_CODE, recallProcessTemplate());
        processController.setTemplates(templates);
        return processController;
    }
}
