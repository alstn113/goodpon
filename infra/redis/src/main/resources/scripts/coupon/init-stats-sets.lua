-- KEYS[1]: Reserved ZSet Key (발급 예약)
-- KEYS[2]: Issued Set Key (발급 확정)
-- KEYS[3]: Redeem Set Key (사용 완료)
-- ARGV[1]: Dummy Value
-- ARGV[2]: TTL (ExpireAt timestamp, -1 if none)

local reserved_key = KEYS[1]
local issued_key = KEYS[2]
local redeem_key = KEYS[3]
local dummy_value = ARGV[1]
local ttl = tonumber(ARGV[2])

if redis.call('EXISTS', reserved_key) == 0 then
    redis.call('ZADD', reserved_key, 0, dummy_value)
    if ttl ~= -1 then
        redis.call('EXPIREAT', reserved_key, ttl)
    end
end

if redis.call('EXISTS', issued_key) == 0 then
    redis.call('SADD', issued_key, dummy_value)
    if ttl ~= -1 then
        redis.call('EXPIREAT', issued_key, ttl)
    end
end

if redis.call('EXISTS', redeem_key) == 0 then
    redis.call('SADD', redeem_key, dummy_value)
    if ttl ~= -1 then
        redis.call('EXPIREAT', redeem_key, ttl)
    end
end

return 1
