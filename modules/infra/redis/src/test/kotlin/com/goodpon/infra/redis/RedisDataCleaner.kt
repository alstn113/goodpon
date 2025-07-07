package com.goodpon.infra.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisDataCleaner(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun clear() {
        redisTemplate
            .connectionFactory!!
            .connection
            .serverCommands()
            .flushDb()
    }
}