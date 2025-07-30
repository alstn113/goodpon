package com.goodpon.infra.redis.coupon

import org.springframework.data.redis.connection.StringRedisConnection
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateStatsCache as Dashboard_CouponTemplateStatsPort
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache as Partner_CouponTemplateStatsPort

@Component
class CouponTemplateStatsRedisCacheAdapter(
    private val stringRedisTemplate: RedisTemplate<String, String>,
) : Dashboard_CouponTemplateStatsPort, Partner_CouponTemplateStatsPort {

    override fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime?) {
        val key = buildKey(couponTemplateId)
        stringRedisTemplate.opsForHash<String, String>().putAll(
            key,
            mapOf(ISSUE_COUNT_KEY to "0", REDEEM_COUNT_KEY to "0")
        )

        expiresAt?.let {
            val expirationWithGracePeriod = expiresAt.plusDays(1) // 만료 후 1일 유예 기간 포함
            val zoneId = ZoneId.of("UTC")
            val instant = expirationWithGracePeriod.atZone(zoneId).toInstant()
            stringRedisTemplate.expireAt(key, Date.from(instant))
        }
    }

    override fun incrementIssueCount(couponTemplateId: Long, limit: Long?): Boolean {
        return executeIncrementWithLimitCheck(couponTemplateId, ISSUE_COUNT_KEY, limit)
    }

    override fun cancelIssue(couponTemplateId: Long): Boolean {
        return executeDecrement(couponTemplateId, ISSUE_COUNT_KEY)
    }

    override fun incrementRedeemCount(couponTemplateId: Long, limit: Long?): Boolean {
        return executeIncrementWithLimitCheck(couponTemplateId, REDEEM_COUNT_KEY, limit)
    }

    override fun cancelRedeem(couponTemplateId: Long): Boolean {
        return executeDecrement(couponTemplateId, REDEEM_COUNT_KEY)
    }

    override fun getStats(couponTemplateId: Long): Pair<Long, Long> {
        val key = buildKey(couponTemplateId)
        val entries = stringRedisTemplate.opsForHash<String, String>().entries(key)
        return parseStats(entries[ISSUE_COUNT_KEY], entries[REDEEM_COUNT_KEY])
    }

    override fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>> {
        val keys = couponTemplateIds.map { buildKey(it) }
        val results = pipelineForStats(keys)
        return couponTemplateIds.mapIndexed { i, id ->
            id to parseStats(results.getOrNull(i * 2), results.getOrNull(i * 2 + 1))
        }.toMap()
    }

    override fun readAllStats(): Map<Long, Pair<Long, Long>> {
        val keys = stringRedisTemplate.keys("$COUPON_TEMPLATE_STATS_KEY_PREFIX*")
        if (keys.isEmpty()) return emptyMap()

        val results = pipelineForStats(keys.toList())

        return keys.mapIndexed { i, key ->
            val couponTemplateId = key.removePrefix(COUPON_TEMPLATE_STATS_KEY_PREFIX).toLong()
            val issueCount = results.getOrNull(i * 2)
            val redeemCount = results.getOrNull(i * 2 + 1)

            couponTemplateId to parseStats(issueCount, redeemCount)
        }.toMap()
    }

    private fun parseStats(issueRaw: Any?, redeemRaw: Any?): Pair<Long, Long> {
        val issueCount = (issueRaw as? String)?.toLongOrNull() ?: 0L
        val redeemCount = (redeemRaw as? String)?.toLongOrNull() ?: 0L
        return Pair(issueCount, redeemCount)
    }

    private fun pipelineForStats(keys: List<String>): List<Any?> {
        return stringRedisTemplate.executePipelined { connection ->
            val stringCon = connection as StringRedisConnection
            keys.forEach { key ->
                stringCon.hGet(key, ISSUE_COUNT_KEY)
                stringCon.hGet(key, REDEEM_COUNT_KEY)
            }
            null
        }
    }

    private fun executeIncrementWithLimitCheck(
        couponTemplateId: Long,
        field: String,
        limit: Long?,
    ): Boolean {
        // 쿠폰 템플릿 통계가 존재하지 않는 경우 -1 -> 예외
        // limit이 null인 경우 ""로 변경해서 전달 => 제한 없음
        // limit이 존재하는 경우, 현재 값 + 1 > limit -> 0 반환 => false
        // limit이 존재하는 경우, 현재 값 + 1 <= limit -> 1 반환 => true
        val script = RedisScript.of(
            """
            local current = redis.call('HGET', KEYS[1], ARGV[1])
            if not current then
                return -1
            end
            current = tonumber(current)
            local limitArg = ARGV[2]
            if limitArg == '' then
                redis.call('HINCRBY', KEYS[1], ARGV[1], 1)
                return 1
            end
            local limit = tonumber(limitArg)
            if (current + 1) > limit then
                return 0
            end
            redis.call('HINCRBY', KEYS[1], ARGV[1], 1)
            return 1
            """.trimIndent(), Long::class.java
        )
        val key = buildKey(couponTemplateId)
        val limitArg = limit?.toString() ?: ""
        return when (val result = stringRedisTemplate.execute(script, listOf(key), field, limitArg)) {
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
        return when (val result = stringRedisTemplate.execute(script, listOf(key), field)) {
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