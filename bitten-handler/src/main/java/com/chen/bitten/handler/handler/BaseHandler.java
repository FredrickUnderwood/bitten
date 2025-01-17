package com.chen.bitten.handler.handler;

import com.chen.bitten.common.domain.AnchorInfo;
import com.chen.bitten.common.domain.TaskInfo;
import com.chen.bitten.common.enums.AnchorStateEnum;
import com.chen.bitten.common.utils.LogUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class BaseHandler implements Handler {

    protected Integer channelType;
    @Autowired
    private HandlerHolder handlerHolder;
    @Autowired
    private LogUtils logUtils;

    @PostConstruct
    public void init() {
        handlerHolder.putHandler(channelType, this);
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {
            logUtils.print(AnchorInfo.builder()
                    .bizId(taskInfo.getBizId()).messageId(taskInfo.getMessageId())
                    .receiver(taskInfo.getReceiver()).state(AnchorStateEnum.SEND_SUCCESS.getCode())
                    .businessId(taskInfo.getBusinessId()).build());
        }
        logUtils.print(AnchorInfo.builder()
                .bizId(taskInfo.getBizId()).messageId(taskInfo.getMessageId())
                .receiver(taskInfo.getReceiver()).state(AnchorStateEnum.SEND_FAIL.getCode())
                .businessId(taskInfo.getBusinessId()).build());
    }

    /**
     * 统一处理的handler接口，具体的推送信息实现类要实现这个接口
     *
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);

}
