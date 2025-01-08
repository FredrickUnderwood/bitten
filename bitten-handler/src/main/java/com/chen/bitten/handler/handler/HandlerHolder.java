package com.chen.bitten.handler.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供 channel -> handler实现类的映射
 */
@Component
public class HandlerHolder {
    private final Map<Integer, Handler> handlers = new HashMap<>(128);
    public void putHandler(Integer channelType, Handler handler) {
        handlers.put(channelType, handler);
    }
    public Handler route(Integer channelType) {
        return handlers.get(channelType);
    }


}
