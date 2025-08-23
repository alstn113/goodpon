-- issueSet 초기화
if redis.call('EXISTS', KEYS[1]) == 0 then
    redis.call('SADD', KEYS[1], ARGV[1])
    if tonumber(ARGV[2]) ~= -1 then
        redis.call('EXPIREAT', KEYS[1], tonumber(ARGV[2]))
    end
end

-- redeemSet 초기화
if redis.call('EXISTS', KEYS[2]) == 0 then
    redis.call('SADD', KEYS[2], ARGV[1])
    if tonumber(ARGV[2]) ~= -1 then
        redis.call('EXPIREAT', KEYS[2], tonumber(ARGV[2]))
    end
end

return 1
