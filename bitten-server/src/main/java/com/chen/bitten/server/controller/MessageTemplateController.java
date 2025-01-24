package com.chen.bitten.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chen.bitten.api.service.SendService;
import com.chen.bitten.common.constant.CommonConstant;
import com.chen.bitten.common.domain.MessageParam;
import com.chen.bitten.common.domain.persistence.MessageTemplate;
import com.chen.bitten.common.dto.MessageTemplateParam;
import com.chen.bitten.common.dto.SendRequest;
import com.chen.bitten.common.dto.SendResponse;
import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.vo.BasicResultVO;
import com.chen.bitten.common.vo.MessageTemplatePageQueryVO;
import com.chen.bitten.server.exception.ResponseException;
import com.chen.bitten.server.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messageTemplate")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SendService sendService;

    @PostMapping("/save")
    public BasicResultVO<MessageTemplate> save(@RequestBody MessageTemplate messageTemplate) {
        return BasicResultVO.success(messageTemplateService.saveOrUpdateMessageTemplate(messageTemplate));
    }

    @GetMapping("/list")
    public BasicResultVO<MessageTemplatePageQueryVO> queryList(MessageTemplateParam messageTemplateParam) {
        MessageTemplatePageQueryVO messageTemplatePageQueryVO = messageTemplateService.queryList(messageTemplateParam);
        return BasicResultVO.success(messageTemplatePageQueryVO);
    }

    @GetMapping("/list/{id}")
    public BasicResultVO<MessageTemplate> queryById(@PathVariable("id") Long id) {
        return BasicResultVO.success(messageTemplateService.queryById(id));
    }

    @PostMapping("/copy/{id}")
    public BasicResultVO copyById(@PathVariable("id") Long id) {
        messageTemplateService.copyById(id);
        return BasicResultVO.success();
    }

    @DeleteMapping("/delete/{ids}")
    public BasicResultVO deleteById(@PathVariable("ids") String ids) {
        if (Objects.nonNull(ids) && !ids.isBlank()) {
            List<Long> idList = Arrays.stream(ids.split(CommonConstant.COMMA)).map(Long::valueOf).collect(Collectors.toList());
            messageTemplateService.deleteByIds(idList);
        }
        return BasicResultVO.success();
    }

    @PostMapping("/test")
    public BasicResultVO<SendResponse> test(@RequestBody MessageTemplateParam messageTemplateParam) {
        Map<String, String> variables = JSON.parseObject(messageTemplateParam.getMessageContent(), new TypeReference<Map<String, String>>() {});
        MessageParam messageParam = MessageParam.builder().receivers(messageTemplateParam.getReceivers()).variables(variables).build();
        SendRequest sendRequest = SendRequest.builder().messageTemplateId(messageTemplateParam.getId())
                .messageParam(messageParam)
                .code("send").build();
        SendResponse sendResponse = sendService.send(sendRequest);
        if (!sendResponse.getCode().equals(RespStatusEnum.SUCCESS.getCode())) {
            throw new ResponseException(sendResponse.getMsg());
        }
        return BasicResultVO.success(sendResponse);
    }
}
