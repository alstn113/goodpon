package com.goodpon.infra.common.redis

import com.goodpon.core.domain.coupon.CouponTemplateStats
import com.goodpon.core.domain.coupon.CouponTemplateStatsCounter
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisCouponTemplateStatsCounter(
    private val redisTemplate: RedisTemplate<String, String>,
) : CouponTemplateStatsCounter {

    override fun getStats(couponTemplateId: Long): CouponTemplateStats {
        val key = generateKey(couponTemplateId)
        val fields = listOf("issueCount", "usageCount")
        val results = redisTemplate.opsForHash<String, String>().multiGet(key, fields)
        val issueCount = results[0]?.toLongOrNull() ?: 0L
        val usageCount = results[1]?.toLongOrNull() ?: 0L

        return CouponTemplateStats(
            couponTemplateId = couponTemplateId,
            issueCount = issueCount,
            usageCount = usageCount
        )
    }

    override fun increaseIssueCount(couponTemplateId: Long, count: Long) {
        val key = generateKey(couponTemplateId)
        redisTemplate.opsForHash<String, String>().increment(key, "issueCount", count)
    }

    override fun increaseUseCount(couponTemplateId: Long, count: Long) {
        val key = generateKey(couponTemplateId)
        redisTemplate.opsForHash<String, String>().increment(key, "usageCount", count)
    }

    private fun generateKey(couponTemplateId: Long): String =
        "coupon:template:stats:$couponTemplateId"
}
