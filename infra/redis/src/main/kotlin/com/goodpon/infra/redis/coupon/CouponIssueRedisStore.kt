package com.goodpon.infra.redis.coupon

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class CouponIssueRedisStore(
    private val redisTemplate: StringRedisTemplate,
    @Qualifier("initStatsSetsScript") private val initStatsSetsScript: RedisScript<Long>,
    @Qualifier("reserveCouponScript") private val reserveCouponScript: RedisScript<Long>,
    @Qualifier("completeIssueCouponScript") private val completeIssueCouponScript: RedisScript<Long>,
    @Qualifier("redeemCouponScript") private val redeemCouponScript: RedisScript<Long>,
) {

    fun initialize(couponTemplateId: Long, expiresAt: LocalDateTime?) {
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        val issuedKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)
        val redeemedKey = CouponRedisKeyUtils.redeemedKey(couponTemplateId)

        val ttlEpochSeconds: Long = expiresAt?.let {
            val expirationWithGrace = it.plusDays(1)
            expirationWithGrace.atZone(ZoneId.of("UTC")).toEpochSecond()
        } ?: -1L

        redisTemplate.execute(
            initStatsSetsScript,
            listOf(reservedKey, issuedKey, redeemedKey),
            "dummy",
            ttlEpochSeconds.toString()
        )
    }

    fun reserve(
        couponTemplateId: Long,
        userId: String,
        maxIssueCount: Long?,
    ): CouponIssueResult {
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        val issuedKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)
        val requestTime = System.currentTimeMillis()

        val result = redisTemplate.execute(
            reserveCouponScript,
            listOf(reservedKey, issuedKey),
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

    fun complete(
        couponTemplateId: Long,
        userId: String,
    ) {
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        val issuedKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)

        redisTemplate.execute(
            completeIssueCouponScript,
            listOf(reservedKey, issuedKey),
            userId
        )
    }

    fun existsReservation(couponTemplateId: Long, userId: String): Boolean {
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        return redisTemplate.opsForZSet().score(reservedKey, userId) != null
    }

    fun markIssueReservationAsFailed(couponTemplateId: Long, userId: String) {
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        redisTemplate.opsForZSet().add(reservedKey, userId, -1.0)
    }

    fun redeem(
        couponTemplateId: Long,
        userId: String,
        maxRedeemCount: Long?,
    ): CouponRedeemResult {
        val redeemedKey = CouponRedisKeyUtils.redeemedKey(couponTemplateId)
        val result = redisTemplate.execute(
            redeemCouponScript,
            listOf(redeemedKey),
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
        val issuedKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)
        redisTemplate.opsForSet().remove(issuedKey, userId)
    }

    fun cancelRedeem(couponTemplateId: Long, userId: String) {
        val redeemedKey = CouponRedisKeyUtils.redeemedKey(couponTemplateId)
        redisTemplate.opsForSet().remove(redeemedKey, userId)
    }
}