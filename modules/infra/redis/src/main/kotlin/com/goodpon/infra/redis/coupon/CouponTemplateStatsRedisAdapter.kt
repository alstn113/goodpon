package com.goodpon.infra.redis.coupon

import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateStatsPort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class CouponTemplateStatsRedisAdapter(
    private val redisTemplate: RedisTemplate<String, Any>,
) : CouponTemplateStatsPort {

    override fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime?) {
        val key = buildKey(couponTemplateId)
        redisTemplate.opsForHash<String, String>().putAll(
            key,
            mapOf(
                ISSUE_COUNT_KEY to "0",
                REDEEM_COUNT_KEY to "0",
            )
        )

        expiresAt?.let {
            val graceExpiresAt = it.plusDays(1)
            val zoneId = ZoneId.of("UTC")
            val instant = graceExpiresAt.atZone(zoneId).toInstant()
            redisTemplate.expireAt(key, Date.from(instant))
        }
    }

    fun incrementIssueCount(couponTemplateId: Long, limit: Long): Boolean {
        return executeIncrementWithLimitCheck(couponTemplateId, ISSUE_COUNT_KEY, limit)
    }

    fun cancelIssue(couponTemplateId: Long): Boolean {
        return executeDecrement(couponTemplateId, ISSUE_COUNT_KEY)
    }

    fun incrementRedeemCount(couponTemplateId: Long, limit: Long): Boolean {
        return executeIncrementWithLimitCheck(couponTemplateId, REDEEM_COUNT_KEY, limit)
    }

    fun cancelRedeem(couponTemplateId: Long): Boolean {
        return executeDecrement(couponTemplateId, REDEEM_COUNT_KEY)
    }

    /**
     * issueCount, redeemCount를 읽고 반환(없으면 0으로 초기화)
     * 발급 또는 사용 시 limit을 초과할 경우 false 반환
     * 초과하지 않을 경우 해당 값을 1 증가시키고 true 반환
     */
    private fun executeIncrementWithLimitCheck(
        couponTemplateId: Long,
        field: String,
        limit: Long,
    ): Boolean {
        val script = RedisScript.of(
            """
            local current = redis.call('HGET', KEYS[1], ARGV[1])
            if not current then
                return -1
            end
            current = tonumber(current)
            local limit = tonumber(ARGV[2])
            if (current + 1) > limit then
                return 0
            end
            redis.call('HINCRBY', KEYS[1], ARGV[1], 1)
            return 1
            """.trimIndent(), Long::class.java
        )
        val key = buildKey(couponTemplateId)
        return when (val result = redisTemplate.execute(script, listOf(key), field, limit.toString())) {
            1L -> true
            0L -> false
            -1L -> throw IllegalStateException("쿠폰 템플릿 $couponTemplateId 의 통계 정보가 존재하지 않습니다.")
            else -> throw IllegalStateException("Redis 쿠폰 템플릿 통계 증가 업데이트 중 예외가 발생했습니다. result: $result")
        }
    }

    private fun executeDecrement(couponTemplateId: Long, field: String): Boolean {
        val script = RedisScript.of(
            """
            local current = redis.call('HGET', KEYS[1], ARGV[1])
            if not current then
                return -1
            end
            current = tonumber(current)
            if current <= 0 then
                return 0
            end
            redis.call('HINCRBY', KEYS[1], ARGV[1], -1)
            return 1
            """.trimIndent(), Long::class.java
        )
        val key = buildKey(couponTemplateId)
        return when (val result = redisTemplate.execute(script, listOf(key), field)) {
            1L -> true
            0L -> false
            -1L -> throw IllegalStateException("쿠폰 템플릿 $couponTemplateId 의 통계 정보가 존재하지 않습니다.")
            else -> throw IllegalStateException("Redis 쿠폰 템플릿 통계 감소 업데이트 중 예외가 발생했습니다. result: $result")
        }
    }

    companion object {
        private const val COUPON_TEMPLATE_STATS_KEY_PREFIX = "coupon-template-stats:"
        private const val ISSUE_COUNT_KEY = "issueCount"
        private const val REDEEM_COUNT_KEY = "redeemCount"

        fun buildKey(couponTemplateId: Long): String {
            return "$COUPON_TEMPLATE_STATS_KEY_PREFIX$couponTemplateId"
        }
    }
}