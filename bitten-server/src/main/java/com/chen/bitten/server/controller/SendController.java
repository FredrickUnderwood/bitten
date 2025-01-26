package com.chen.bitten.server.controller;

import com.chen.bitten.api.service.RecallService;
import com.chen.bitten.api.service.SendService;
import com.chen.bitten.common.dto.BatchSendRequest;
import com.chen.bitten.common.dto.SendRequest;
import com.chen.bitten.common.dto.SendResponse;
import com.chen.bitten.common.vo.BasicResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SendController {

    @Autowired
    private SendService sendService;

    @Autowired
    private RecallService recallService;


    @PostMapping("/send")
    public BasicResultVO<SendResponse> send(@RequestBody SendRequest sendRequest) {
        return BasicResultVO.success(sendService.send(sendRequest));
    }

    @PostMapping("/batchSend")
    public BasicResultVO<SendResponse> batchSend(@RequestBody BatchSendRequest batchSendRequest) {
        return BasicResultVO.success(sendService.batchSend(batchSendRequest));
    }

    @PostMapping("/recall")
    public BasicResultVO<SendResponse> recall(@RequestBody SendRequest sendRequest) {
        return BasicResultVO.success(recallService.recall(sendRequest));
    }
}
