package com.chen.bitten.server.controller;

import cn.hutool.core.util.IdUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/messageTemplate")
public class MessageTemplateController {

    private static final String LOG_PREFIX = "[MessageTemplateController]";

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SendService sendService;

    @Value("${bitten.dataPath}")
    private String dataPath;

    @PostMapping("/save")
    public BasicResultVO<MessageTemplate> save(@RequestBody MessageTemplate messageTemplate) {
        return BasicResultVO.success(messageTemplateService.saveOrUpdateMessageTemplate(messageTemplate));
    }

    @GetMapping("/list")
    public BasicResultVO<MessageTemplatePageQueryVO> queryList(MessageTemplateParam messageTemplateParam) {
        MessageTemplatePageQueryVO messageTemplatePageQueryVO = messageTemplateService.queryList(messageTemplateParam);
        return BasicResultVO.success(messageTemplatePageQueryVO);
    }

    @GetMapping("/query/{id}")
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
        MessageParam messageParam = MessageParam.builder().receivers(messageTemplateParam.getReceiver()).variables(variables).build();
        SendRequest sendRequest = SendRequest.builder().messageTemplateId(messageTemplateParam.getId())
                .messageParam(messageParam)
                .code("send").build();
        SendResponse sendResponse = sendService.send(sendRequest);
        if (!sendResponse.getCode().equals(RespStatusEnum.SUCCESS.getCode())) {
            throw new ResponseException(sendResponse.getMsg());
        }
        return BasicResultVO.success(sendResponse);
    }
    @PostMapping("/test/content")
    public BasicResultVO<String> test(Long id) {
        return BasicResultVO.success(messageTemplateService.queryById(id).getMessageContent());
    }

    @PostMapping("/start/{id}")
    public BasicResultVO startCronTask(@PathVariable("id") Long id) {
        return messageTemplateService.startCronTask(id);
    }

    @PostMapping("/stop/{id}")
    public BasicResultVO stopCronTask(@PathVariable("id") Long id) {
        return messageTemplateService.stopCronTask(id);
    }

    /**
     * 上传定时任务参数文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public BasicResultVO<String> upload(@RequestParam("file")MultipartFile file) {
        String filePath = dataPath + IdUtil.fastSimpleUUID() + file.getOriginalFilename();
        try {
            File localFile = new File(filePath);
            if (!localFile.exists()) {
                boolean res = localFile.mkdir();
                if (!res) {
                    log.error("{}Make directory fail!", LOG_PREFIX);
                    throw new ResponseException(RespStatusEnum.SERVER_ERROR);
                }
            }
            file.transferTo(localFile);
        } catch (IOException e) {
            log.error("{}upload fail! e: {}", LOG_PREFIX, e.getStackTrace());
            throw new ResponseException(RespStatusEnum.SERVER_ERROR);
        }
        return BasicResultVO.success(filePath);
    }

}
