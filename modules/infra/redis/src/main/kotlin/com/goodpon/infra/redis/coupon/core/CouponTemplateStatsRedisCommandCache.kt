package com.goodpon.infra.redis.coupon.core

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
    @Qualifier("reserveCouponScript") private val reserveCouponScript: RedisScript<Long>,
    @Qualifier("completeIssueCouponScript") private val completeIssueCouponScript: RedisScript<Long>,
    @Qualifier("redeemCouponScript") private val redeemCouponScript: RedisScript<Long>,
) {

    fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime?) {
        val reservedSetKey = CouponTemplateRedisKeyUtil.buildReservedSetKey(couponTemplateId)
        val issuedSetKey = CouponTemplateRedisKeyUtil.buildIssuedSetKey(couponTemplateId)
        val redeemedSetKey = CouponTemplateRedisKeyUtil.buildRedeemedSetKey(couponTemplateId)

        val ttlEpochSeconds: Long = expiresAt?.let {
            val expirationWithGrace = it.plusDays(1)
            expirationWithGrace.atZone(ZoneId.of("UTC")).toEpochSecond()
        } ?: -1L

        redisTemplate.execute(
            initStatsSetsScript,
            listOf(reservedSetKey, issuedSetKey, redeemedSetKey),
            "dummy",
            ttlEpochSeconds.toString()
        )
    }

    fun reserveCoupon(
        couponTemplateId: Long,
        userId: String,
        maxIssueCount: Long?,
    ): CouponIssueResult {
        val reservedSetKey = CouponTemplateRedisKeyUtil.buildReservedSetKey(couponTemplateId)
        val issuedSetKey = CouponTemplateRedisKeyUtil.buildIssuedSetKey(couponTemplateId)
        val requestTime = System.currentTimeMillis()

        val result = redisTemplate.execute(
            reserveCouponScript,
            listOf(reservedSetKey, issuedSetKey),
            userId,
            maxIssueCount?.toString() ?: "-1",
            requestTime.toString()
        )

        return when (result) {
            0L -> CouponIssueResult.SUCCESS
            1L -> CouponIssueResult.ALREADY_ISSUED
            2L -> CouponIssueResult.ISSUE_LIMIT_EXCEEDED
            else -> throw IllegalStateException("reserve coupon redis script에서 예상치 못한 결과가 발생했습니다. result: $result")
        }
    }

    fun completeIssueCoupon(
        couponTemplateId: Long,
        userId: String,
    ) {
        val reservedSetKey = CouponTemplateRedisKeyUtil.buildReservedSetKey(couponTemplateId)
        val issuedSetKey = CouponTemplateRedisKeyUtil.buildIssuedSetKey(couponTemplateId)

        redisTemplate.execute(
            completeIssueCouponScript,
            listOf(reservedSetKey, issuedSetKey),
            userId
        )
    }

    fun redeemCoupon(
        couponTemplateId: Long,
        userId: String,
        maxRedeemCount: Long?,
    ): CouponRedeemResult {
        val key = CouponTemplateRedisKeyUtil.buildRedeemedSetKey(couponTemplateId)
        val result = redisTemplate.execute(
            redeemCouponScript,
            listOf(key),
            userId,
            maxRedeemCount?.toString() ?: "-1"
        )

        return when (result) {
            0L -> CouponRedeemResult.SUCCESS
            1L -> CouponRedeemResult.ALREADY_REDEEMED
            2L -> CouponRedeemResult.REDEEM_LIMIT_EXCEEDED
            else -> throw IllegalStateException("coupon redeem redis script에서 예상치 못한 결과가 발생했습니다. result: $result")
        }
    }

    fun cancelIssue(couponTemplateId: Long, userId: String) {
        val key = CouponTemplateRedisKeyUtil.buildIssuedSetKey(couponTemplateId)
        redisTemplate.opsForSet().remove(key, userId)
    }

    fun cancelRedeem(couponTemplateId: Long, userId: String) {
        val key = CouponTemplateRedisKeyUtil.buildRedeemedSetKey(couponTemplateId)
        redisTemplate.opsForSet().remove(key, userId)
    }
}