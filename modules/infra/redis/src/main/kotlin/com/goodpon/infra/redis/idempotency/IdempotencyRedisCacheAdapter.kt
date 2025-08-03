package com.goodpon.infra.redis.idempotency

import com.goodpon.application.partner.idempotency.port.out.IdempotencyCachePort
import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse
import com.goodpon.application.partner.idempotency.service.IdempotencyStatus
import com.goodpon.application.partner.idempotency.service.IdempotencyValue
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

    override fun check(key: String, requestHash: String): IdempotencyCheckResult {
        val raw = redisTemplate.opsForValue().get(key)
            ?: return IdempotencyCheckResult.NotExists
        val stored = objectMapper.readValue(raw, IdempotencyValue::class.java)

        return when (stored.status) {
            IdempotencyStatus.PROCESSING -> IdempotencyCheckResult.Processing
            IdempotencyStatus.COMPLETED -> {
                if (stored.requestHash != requestHash) IdempotencyCheckResult.Conflict
                else IdempotencyCheckResult.Completed(stored.response!!)
            }
        }
    }

    override fun saveProcessing(key: String, requestHash: String) {
        val value = IdempotencyValue(
            requestHash = requestHash,
            status = IdempotencyStatus.PROCESSING,
            response = null
        )

        redisTemplate.opsForValue().set(
            key,
            objectMapper.writeValueAsString(value),
            PROCESSING_DURATION
        )
    }

    override fun saveCompleted(key: String, response: IdempotencyResponse) {
        val raw = redisTemplate.opsForValue().get(key)
            ?: throw IllegalStateException("멱등성 키($key)가 존재하지 않습니다.")

        val value = objectMapper.readValue(raw, IdempotencyValue::class.java)
            .copy(status = IdempotencyStatus.COMPLETED, response = response)

        redisTemplate.opsForValue().set(
            key,
            objectMapper.writeValueAsString(value),
            COMPLETED_DURATION
        )
    }

    companion object {
        private val PROCESSING_DURATION: Duration = Duration.ofHours(1)
        private val COMPLETED_DURATION: Duration = Duration.ofHours(24)
    }
}