package com.chen.bitten.handler.handler;

import com.chen.bitten.common.domain.TaskInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class BaseHandler implements Handler{

    protected Integer channelType;
    @Autowired
    private HandlerHolder handlerHolder;

    @PostConstruct
    public void init() {
        handlerHolder.putHandler(channelType, this);
    }

    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {
            // TODO 推送信息成功，日志记录
        }
        // TODO 推送消息失败，日志记录
    }

    /**
     * 统一处理的handler接口，具体的推送信息实现类要实现这个接口
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);

}
