-- KEYS[1]: Issued Set Key
-- KEYS[2]: Reserved ZSet Key
-- ARGV[1]: userId

local issued_key = KEYS[1]
local reserved_key = KEYS[2]
local user_id = ARGV[1]

-- 예약 목록에서 제거
redis.call('ZREM', reserved_key, user_id)

-- 발급 완료 목록에 추가
redis.call('SADD', issued_key, user_id)

return 0
