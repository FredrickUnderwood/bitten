package com.chen;

import com.chen.bitten.api.service.SendService;
import com.chen.bitten.common.domain.MessageParam;
import com.chen.bitten.common.dto.SendRequest;
import com.chen.bitten.common.dto.SendResponse;
import com.chen.bitten.common.process.ProcessContext;
import com.chen.bitten.server.BittenApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BittenApplication.class)
public class SendServiceTest {

    @Autowired
    private SendService sendService;


    @Test
    void send() throws InterruptedException {
        MessageParam messageParam = MessageParam.builder()
                .bizId("testBiz")
                .receivers("3314333502@qq.com").build();
        SendRequest sendRequest = SendRequest.builder()
                .code("send")
                .messageParam(messageParam)
                .messageTemplateId(1L).build();
        SendResponse sendResponse = sendService.send(sendRequest);
        System.out.println(sendResponse);
        Thread.sleep(10000);
    }

}
