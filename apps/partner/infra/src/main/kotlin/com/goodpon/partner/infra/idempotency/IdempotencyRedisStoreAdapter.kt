package com.goodpon.partner.infra.idempotency

import com.goodpon.infra.redis.config.RedisConfig
import com.goodpon.partner.application.idempotency.port.out.IdempotencyStore
import com.goodpon.partner.application.idempotency.service.IdempotencyCheckResult
import com.goodpon.partner.application.idempotency.service.IdempotencyEntry
import com.goodpon.partner.application.idempotency.service.IdempotencyResponse
import com.goodpon.partner.application.idempotency.service.IdempotencyStatus
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class IdempotencyRedisStoreAdapter(
    private val redisTemplate: StringRedisTemplate,
    private val redisConfig: RedisConfig,
) : IdempotencyStore {

    private val objectMapper = redisConfig.redisObjectMapper()

    override fun checkOrMarkAsProcessing(key: String, hashedRequestBody: String): IdempotencyCheckResult {
        val entry = IdempotencyEntry(hashedRequestBody, IdempotencyStatus.PROCESSING, null)
        val success = redisTemplate.opsForValue().setIfAbsent(
            key,
            objectMapper.writeValueAsString(entry),
            TTL_PROCESSING,
        )
        if (success == true) return IdempotencyCheckResult.FirstRequestProcessing

        val raw = redisTemplate.opsForValue().get(key)
        val stored = objectMapper.readValue(raw, IdempotencyEntry::class.java)

        return when (stored.status) {
            IdempotencyStatus.PROCESSING -> IdempotencyCheckResult.CurrentlyProcessing
            IdempotencyStatus.COMPLETED -> {
                if (stored.requestHash != hashedRequestBody) IdempotencyCheckResult.RequestBodyMismatch
                else IdempotencyCheckResult.AlreadyCompleted(stored.response!!)
            }
        }
    }

    override fun markAsCompleted(key: String, response: IdempotencyResponse) {
        val raw = redisTemplate.opsForValue().get(key)
            ?: throw IllegalStateException("멱등성 키($key)가 존재하지 않습니다.")

        val updated = objectMapper.readValue(raw, IdempotencyEntry::class.java)
            .copy(status = IdempotencyStatus.COMPLETED, response = response)

        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(updated), TTL_COMPLETED)
    }

    override fun clearProcessing(key: String) {
        redisTemplate.delete(key)
    }

    companion object {
        private val TTL_PROCESSING: Duration = Duration.ofMinutes(10)
        private val TTL_COMPLETED: Duration = Duration.ofHours(24)
    }
}