package com.chen.bitten.data.utils;

import com.chen.bitten.data.constant.BittenDataConstant;
import com.chen.bitten.data.callback.RedisPipelineCallBack;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.ByteArrayCodec;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisUtils {

    private static final RedisClient REDIS_CLIENT;

    static {
        RedisURI redisURI = RedisURI.builder().redis(BittenDataConstant.HOST)
                .withPort(BittenDataConstant.PORT)
                .withDatabase(BittenDataConstant.DATABASE).build();
        REDIS_CLIENT = RedisClient.create(redisURI);
    }

    /**
     * 封装pipeline操作
     */
    public static void pipeline(RedisPipelineCallBack redisPipelineCallBack) {
        StatefulRedisConnection<byte[], byte[]> connection = REDIS_CLIENT.connect(new ByteArrayCodec());
        RedisAsyncCommands<byte[], byte[]> commands = connection.async();
        List<RedisFuture<?>> futures = redisPipelineCallBack.invoke(commands);
        connection.flushCommands();
        LettuceFutures.awaitAll(10, TimeUnit.SECONDS, futures.toArray(new RedisFuture[0]));
        connection.close();
    }

}
