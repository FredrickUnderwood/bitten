

package com.chen.bitten.handler.handler.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.dto.model.EmailContentModel;
import com.chen.bitten.common.enums.ChannelTypeEnum;
import com.chen.bitten.common.utils.AccountUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailHandlerTest {

    @Mock
    private AccountUtils accountUtils;

    @InjectMocks
    private EmailHandler emailHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandler_Success() {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setSendAccount(1);
        Set<String> receiver = new HashSet<>();
        receiver.add("3314333502@qq.com");
        taskInfo.setReceiver(receiver);
        taskInfo.setSendChannel(ChannelTypeEnum.EMAIL.getCode());

        EmailContentModel emailContentModel = new EmailContentModel();
        emailContentModel.setTitle("Test Email");
        emailContentModel.setContent("This is a test email.");
        taskInfo.setContentModel(emailContentModel);

        MailAccount mailAccount = JSON.parseObject("{\"from\": \"1519995555@qq.com\",\"pass\": \"hfkphvldbjsehdgj\",\"port\":465,\"host\":\"smtp.qq.com\",\"sslEnable\":true}", MailAccount
                .class);
        when(accountUtils.getAccountById(1, MailAccount.class)).thenReturn(mailAccount);

        boolean result = emailHandler.handler(taskInfo);
        assertTrue(result);
    }
}
