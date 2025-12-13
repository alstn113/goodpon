-- KEYS[1]: Reserved ZSet Key (예약 대기)
-- KEYS[2]: Issued Set Key (발급 완료)
-- ARGV[1]: userId
-- ARGV[2]: maxIssueCount (제한 없음: -1)
-- ARGV[3]: requestTime (현재 타임스탬프)

local reserved_key = KEYS[1]
local issued_key = KEYS[2]
local user_id = ARGV[1]
local max_issue_count = tonumber(ARGV[2])
local requestTime = ARGV[3]

-- 이미 발급 완료된 사용자인 경우
if redis.call('SISMEMBER', issued_key, user_id) == 1 then
    return 1 -- ALREADY_ISSUED (발급 상태)
end

-- 이미 예약 리스트에 있는지 확인
local current_score = redis.call('ZSCORE', reserved_key, user_id)

-- 값이 존재하면 해당 유저는 이미 예약된 상태
-- nil만 false 처리됨 음수도 true
if current_score then
    return 1 -- ALREADY_ISSUED (예약 상태)
end

if max_issue_count ~= -1 then
    local issued_count = redis.call('SCARD', issued_key) - 1
    local reserved_count = redis.call('ZCARD', reserved_key) - 1

    if (issued_count + reserved_count) >= max_issue_count then
        return 2 -- ISSUE_LIMIT_EXCEEDED
    end
end

redis.call('ZADD', reserved_key, requestTime, user_id)

return 0 -- SUCCESS