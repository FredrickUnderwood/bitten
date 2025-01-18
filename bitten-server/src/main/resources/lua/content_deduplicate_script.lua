-- KEYS[1]: key
-- ARGV[1]: 去重窗口时间
-- ARGV[2]: 当前时间
-- ARGV[3]: 去重次数上限
-- ARGV[4]: scoreValue，唯一值
redis.call('ZREMRANGEBYSCORE', KEYS[1], 0, ARGV[2] - ARGV[1])
local res = redis.call('ZCARD', KEYS[1])
if (res == nil) or (res < tonumber(ARGV[3])) then
    redis.call('ZADD', KEYS[1], ARGV[2], ARGV[4])
    redis.call('EXPIRE', KEYS[1], ARGV[1] / 1000)
    return 0
else
    return 1
end