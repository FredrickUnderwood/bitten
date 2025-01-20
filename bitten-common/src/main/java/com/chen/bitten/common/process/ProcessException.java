package com.chen.bitten.common.process;

import com.chen.bitten.common.enums.RespStatusEnum;

import java.util.Objects;

public class ProcessException extends RuntimeException {


    private final ProcessContext processContext;

    public ProcessException(ProcessContext processContext) {
        super();
        this.processContext = processContext;
    }

    public ProcessException(ProcessContext processContext, Throwable cause) {
        super(cause);
        this.processContext = processContext;
    }

    @Override
    public String getMessage() {
        if (Objects.nonNull(processContext)) {
            return processContext.getResponse().getMsg();
        } else {
            return RespStatusEnum.CONTEXT_NULL.getMsg();
        }
    }

    public ProcessContext getProcessContext() {
        return processContext;
    }

}
