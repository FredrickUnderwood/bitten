package com.chen.bitten.handler.handler.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.dto.model.EmailContentModel;
import com.chen.bitten.common.enums.ChannelTypeEnum;
import com.chen.bitten.common.utils.AccountUtils;
import com.chen.bitten.handler.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class EmailHandler extends BaseHandler {

    private final static String LOG_PREFIX = "[EmailHandler]";

    @Autowired
    private AccountUtils accountUtils;

    public EmailHandler() {
        this.channelType = ChannelTypeEnum.EMAIL.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount mailAccount = accountUtils.getAccountById(taskInfo.getSendAccount(), MailAccount.class);
        try {
            // TODO BittenFileUtils和文件上传
            MailUtil.send(mailAccount, taskInfo.getReceiver(), emailContentModel.getTitle(), emailContentModel.getContent(), true);
        } catch (Exception e) {
            log.error("{}handler fail! e: {}", LOG_PREFIX, e.getStackTrace());
            return false;
        }
        return true;
    }
}
