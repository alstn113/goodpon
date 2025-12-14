local key = KEYS[1]
local userId = ARGV[1]
local maxRedeemCount = tonumber(ARGV[2])

-- 이미 사용한 사용자인 경우
if redis.call('SISMEMBER', key, userId) == 1 then
    return 1
end

-- 사용 제한 수량을 초과한 경우
local redeemCount = redis.call('SCARD', key)
if maxRedeemCount ~= -1 and redeemCount - 1 >= maxRedeemCount then -- dummy 포함이라 -1처리
    return 2
end

-- 사용 처리
redis.call('SADD', key, userId)
return 0
