local key = KEYS[1]
local userId = ARGV[1]
local maxIssueCount = tonumber(ARGV[2])

-- 이미 발급한 사용자인 경우
if redis.call('SISMEMBER', key, userId) == 1 then
    return 1
end

-- 발급 제한 수량을 초과한 경우
local issueCount = redis.call('SCARD', key)
if maxIssueCount ~= -1 and issueCount-1 >= maxIssueCount then -- dummy 포함이라 -1처리
    return 2
end

-- 발급 처리
redis.call('SADD', key, userId)
return 0