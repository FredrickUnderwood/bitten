package com.chen.bitten.handler.handler.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.chen.bitten.common.domain.RecallTaskInfo;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.dto.model.EmailContentModel;
import com.chen.bitten.common.enums.ChannelTypeEnum;
import com.chen.bitten.common.enums.RateLimitStrategy;
import com.chen.bitten.common.utils.AccountUtils;
import com.chen.bitten.handler.flowcontrol.FlowControlParam;
import com.chen.bitten.handler.handler.BaseHandler;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Component
public class EmailHandler extends BaseHandler {

    private final static String LOG_PREFIX = "[EmailHandler]";

    private final static String MAIL_SSL_SOCKET_FACTORY_PROPERTY_KEY = "mail.smtp.ssl.socketFactory";

    @Autowired
    private AccountUtils accountUtils;

    public EmailHandler() {
        this.channelType = ChannelTypeEnum.EMAIL.getCode();
        double rateLimitInitValue = 3.0;
        this.flowControlParam = FlowControlParam.builder()
                .rateLimiter(RateLimiter.create(rateLimitInitValue))
                .rateLimitInitValue(rateLimitInitValue)
                .rateLimitStrategy(RateLimitStrategy.REQUEST_RATE_LIMIT).build();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount mailAccount = getMailAccount(taskInfo.getSendAccount());
        try {
            // TODO BittenFileUtils和文件上传
            MailUtil.send(mailAccount, taskInfo.getReceiver(), emailContentModel.getTitle(), emailContentModel.getContent(), true);
        } catch (Exception e) {
            log.error("{}handler fail! e: {}", LOG_PREFIX, e.getStackTrace());
            return false;
        }
        return true;
    }

    private MailAccount getMailAccount(Integer accountId) {
        MailAccount mailAccount = accountUtils.getAccountById(accountId, MailAccount.class);
        try {
            MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(true);
            mailAccount.setAuth(mailAccount.isAuth()).setStarttlsEnable(mailAccount.isStarttlsEnable()).setCustomProperty(MAIL_SSL_SOCKET_FACTORY_PROPERTY_KEY, mailSSLSocketFactory);
            mailAccount.setTimeout(25000).setConnectionTimeout(25000);
        } catch (GeneralSecurityException e) {
            log.info("{}getEmailHandler fail! e: {}", LOG_PREFIX, e.getStackTrace());
        }
        return mailAccount;
    }

    /**
     * 无法撤回消息
     * @param recallTaskInfo
     */
    @Override
    public void recall(RecallTaskInfo recallTaskInfo) {

    }
}
