package com.chen.bitten.server.controller;

import com.chen.bitten.common.dto.DataParam;
import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.common.vo.UserTimeLineVO;
import com.chen.bitten.server.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/trace")
public class DataController {

    @Autowired
    private DataService dataService;

    /**
    *  messageId维度
     */
    @PostMapping("/message")
    public BasicResultVO<UserTimeLineVO> getMessageData(@RequestBody DataParam dataParam) {
        if (Objects.isNull(dataParam) || Objects.isNull(dataParam.getMessageId()) || dataParam.getMessageId().isBlank()) {
            return BasicResultVO.success(UserTimeLineVO.builder().items(new ArrayList<>()).build());
        }
        return BasicResultVO.success(dataService.getMessageTrace(dataParam.getMessageId()));
    }

    /**
     * receiver维度
     */
    @PostMapping("/user")
    public BasicResultVO<UserTimeLineVO> getUserData(@RequestBody DataParam dataParam) {
        if (Objects.isNull(dataParam) || Objects.isNull(dataParam.getReceiver()) || dataParam.getReceiver().isBlank()) {
            return BasicResultVO.success(UserTimeLineVO.builder().items(new ArrayList<>()).build());
        }
        return BasicResultVO.success(dataService.getUserTrace(dataParam.getReceiver()));
    }


}
