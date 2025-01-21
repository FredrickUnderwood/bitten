package com.chen.bitten.data.callback;


import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.List;

public interface RedisPipelineCallBack {

    List<RedisFuture<?>> invoke(RedisAsyncCommands redisAsyncCommands);
}
