package com.chen.bitten.server.exception;

import com.chen.bitten.common.enums.RespStatusEnum;
import com.chen.bitten.common.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@ControllerAdvice(basePackages = "com.chen.bitten.server.controller")
@ResponseBody
public class ResponseExceptionHandler {

    private static final String LOG_PREFIX = "[ResponseExceptionHandler]";

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public BasicResultVO<String> exceptionHandler(Exception e) {
        log.error("{}Exception e: {}", LOG_PREFIX, e.getStackTrace());
        return BasicResultVO.fail(RespStatusEnum.ERROR_500, "\r\n" + Arrays.toString(e.getStackTrace()) + "\r\n");
    }

    @ExceptionHandler({ResponseException.class})
    @ResponseStatus(HttpStatus.OK)
    public BasicResultVO<RespStatusEnum> responseExceptionHandler(ResponseException e) {
        log.error("{}ResponseException e: {}", LOG_PREFIX, e.getStackTrace());
        return new BasicResultVO<>(e.getCode(), e.getMessage(), e.getRespStatus());
    }
}
