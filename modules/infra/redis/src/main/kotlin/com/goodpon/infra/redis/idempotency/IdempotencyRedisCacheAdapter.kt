package com.goodpon.infra.redis.idempotency

import com.goodpon.application.partner.idempotency.port.out.IdempotencyCachePort
import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyEntry
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse
import com.goodpon.application.partner.idempotency.service.IdempotencyStatus
import com.goodpon.infra.redis.config.RedisConfig
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class IdempotencyRedisCacheAdapter(
    private val redisTemplate: StringRedisTemplate,
    private val redisConfig: RedisConfig,
) : IdempotencyCachePort {

    private val objectMapper = redisConfig.redisObjectMapper()

    override fun validateKey(key: String, requestHash: String): IdempotencyCheckResult {
        val raw = redisTemplate.opsForValue().get(key)
            ?: return IdempotencyCheckResult.NotFound
        val stored = objectMapper.readValue(raw, IdempotencyEntry::class.java)

        return when (stored.status) {
            IdempotencyStatus.PROCESSING -> IdempotencyCheckResult.Processing
            IdempotencyStatus.COMPLETED -> {
                if (stored.requestHash != requestHash) IdempotencyCheckResult.Conflict
                else IdempotencyCheckResult.Completed(stored.response!!)
            }
        }
    }

    override fun markAsProcessing(key: String, requestHash: String) {
        val entry = IdempotencyEntry(requestHash, IdempotencyStatus.PROCESSING, null)
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(entry), TTL_PROCESSING)
    }

    override fun markAsCompleted(key: String, response: IdempotencyResponse) {
        val raw = redisTemplate.opsForValue().get(key)
            ?: throw IllegalStateException("멱등성 키($key)가 존재하지 않습니다.")

        val updated = objectMapper.readValue(raw, IdempotencyEntry::class.java)
            .copy(status = IdempotencyStatus.COMPLETED, response = response)

        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(updated), TTL_COMPLETED)
    }

    companion object {
        private val TTL_PROCESSING: Duration = Duration.ofHours(1)
        private val TTL_COMPLETED: Duration = Duration.ofHours(24)
    }
}