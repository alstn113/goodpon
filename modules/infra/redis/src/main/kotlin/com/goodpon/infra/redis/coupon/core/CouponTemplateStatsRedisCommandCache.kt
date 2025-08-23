package com.goodpon.infra.redis.coupon.core

import com.goodpon.application.partner.coupon.port.out.dto.IssueResult
import com.goodpon.application.partner.coupon.port.out.dto.RedeemResult
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class CouponTemplateStatsRedisCommandCache(
    private val redisTemplate: StringRedisTemplate,
    @Qualifier("initStatsSetsScript") private val initStatsSetsScript: RedisScript<Long>,
    @Qualifier("issueCouponScript") private val issueCouponScript: RedisScript<Long>,
    @Qualifier("redeemCouponScript") private val redeemCouponScript: RedisScript<Long>,
) {

    fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime?) {
        val issueSetKey = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)
        val redeemSetKey = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)

        val ttlEpochSeconds: Long = expiresAt?.let {
            val expirationWithGrace = it.plusDays(1)
            expirationWithGrace.atZone(ZoneId.of("UTC")).toEpochSecond()
        } ?: -1L

        redisTemplate.execute(
            initStatsSetsScript,
            listOf(issueSetKey, redeemSetKey),
            "dummy",
            ttlEpochSeconds.toString()
        )
    }

    fun issueCoupon(couponTemplateId: Long, userId: String, maxIssueCount: Long?): IssueResult {
        val key = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)
        val result = redisTemplate.execute(
            issueCouponScript,
            listOf(key),
            userId,
            maxIssueCount?.toString() ?: "-1"
        )

        return when (result) {
            0L -> IssueResult.SUCCESS
            1L -> IssueResult.ALREADY_ISSUED
            2L -> IssueResult.ISSUE_LIMIT_EXCEEDED
            else -> throw IllegalStateException("coupon issue redis script에서 예상치 못한 결과가 발생했습니다. result: $result")
        }
    }

    fun redeemCoupon(couponTemplateId: Long, userId: String, maxRedeemCount: Long?): RedeemResult {
        val key = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)
        val result = redisTemplate.execute(
            redeemCouponScript,
            listOf(key),
            userId,
            maxRedeemCount?.toString() ?: "-1"
        )

        return when (result) {
            0L -> RedeemResult.SUCCESS
            1L -> RedeemResult.ALREADY_REDEEMED
            2L -> RedeemResult.REDEEM_LIMIT_EXCEEDED
            else -> throw IllegalStateException("coupon redeem redis script에서 예상치 못한 결과가 발생했습니다. result: $result")
        }
    }

    fun cancelIssue(couponTemplateId: Long, userId: String) {
        val key = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)
        redisTemplate.opsForSet().remove(key, userId)
    }

    fun cancelRedeem(couponTemplateId: Long, userId: String) {
        val key = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)
        redisTemplate.opsForSet().remove(key, userId)
    }
}