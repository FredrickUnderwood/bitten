package com.chen.bitten.server.exception;

import com.chen.bitten.common.enums.RespStatusEnum;
import lombok.Data;
import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException {
    private String code;
    private RespStatusEnum respStatus = null;

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(RespStatusEnum respStatus) {
        super(respStatus.getMsg());
        this.code = respStatus.getCode();
        this.respStatus = respStatus;
    }

    public ResponseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ResponseException(String message, Exception e) {
        super(message, e);
    }

    public ResponseException(String message, Exception e, String code) {
        super(message, e);
        this.code = code;
    }
}
