package com.goodpon.infra.redis.coupon

import com.goodpon.infra.redis.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.LocalDateTime
import kotlin.test.Test

class CouponIssueRedisStoreIT(
    private val couponIssueRedisStore: CouponIssueRedisStore,
    private val redisTemplate: StringRedisTemplate,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰 발급과 사용 집합을 초기화할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val expiresAt = LocalDateTime.now().plusDays(1)

        // when
        couponIssueRedisStore.initialize(couponTemplateId, expiresAt)

        // then
        val issueSetKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)
        redisTemplate.hasKey(issueSetKey) shouldBe true
        redisTemplate.opsForSet().size(issueSetKey) shouldBe 1L // dummy

        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        redisTemplate.hasKey(reservedKey) shouldBe true
        redisTemplate.opsForZSet().size(reservedKey) shouldBe 1L // dummy

        val redeemSetKey = CouponRedisKeyUtils.redeemedKey(couponTemplateId)
        redisTemplate.hasKey(redeemSetKey) shouldBe true
        redisTemplate.opsForSet().size(redeemSetKey) shouldBe 1L // dummy
    }

    @Test
    fun `만료 시간이 없는 경우에도 쿠폰 발급과 사용 집합을 초기화할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val expiresAt = null

        // when
        couponIssueRedisStore.initialize(couponTemplateId, expiresAt)

        // then
        val issueSetKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)
        redisTemplate.hasKey(issueSetKey) shouldBe true
        redisTemplate.opsForSet().size(issueSetKey) shouldBe 1L // dummy

        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        redisTemplate.hasKey(reservedKey) shouldBe true
        redisTemplate.opsForZSet().size(reservedKey) shouldBe 1L // dummy

        val redeemSetKey = CouponRedisKeyUtils.redeemedKey(couponTemplateId)
        redisTemplate.hasKey(redeemSetKey) shouldBe true
        redisTemplate.opsForSet().size(redeemSetKey) shouldBe 1L // dummy
    }

    @Test
    fun `쿠폰 발급 요청(Reserve)을 할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        couponIssueRedisStore.initialize(couponTemplateId, null)

        // when
        val result = couponIssueRedisStore.reserve(couponTemplateId, userId, null)

        // then
        result shouldBe CouponIssueResult.SUCCESS
        redisTemplate.opsForZSet().size(reservedKey) shouldBe 2L
    }

    @Test
    fun `쿠폰 발급 완료(Issued) 처리할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        val issuedKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)

        couponIssueRedisStore.initialize(couponTemplateId, null)
        couponIssueRedisStore.reserve(couponTemplateId, userId, null)

        // when
        couponIssueRedisStore.complete(couponTemplateId, userId)

        // then
        redisTemplate.opsForZSet().size(reservedKey) shouldBe 1L
        redisTemplate.opsForSet().size(issuedKey) shouldBe 2L
        redisTemplate.opsForSet().isMember(issuedKey, userId) shouldBe true
    }

    @Test
    fun `쿠폰 발급 예약이 있는 경우를 확인할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"

        couponIssueRedisStore.initialize(couponTemplateId, null)
        couponIssueRedisStore.reserve(couponTemplateId, userId, null)

        // when
        val hasReservation = couponIssueRedisStore.existsReservation(couponTemplateId, userId)

        // then
        hasReservation shouldBe true
    }

    @Test
    fun `쿠폰 발급 예약이 없는 경우를 확인할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        couponIssueRedisStore.initialize(couponTemplateId, null)

        // when
        val hasReservation = couponIssueRedisStore.existsReservation(couponTemplateId, userId)

        // then
        hasReservation shouldBe false
    }

    @Test
    fun `쿠폰 발급 실패로 표시할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)

        couponIssueRedisStore.initialize(couponTemplateId, null)
        couponIssueRedisStore.reserve(couponTemplateId, userId, null)

        // when
        couponIssueRedisStore.markIssueReservationAsFailed(couponTemplateId, userId)

        // then
        val score = redisTemplate.opsForZSet().score(reservedKey, userId)
        score shouldBe -1.0
    }

    @Test
    fun `쿠폰 발급 시 중복 발급을 방지할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"

        couponIssueRedisStore.initialize(couponTemplateId, null)
        val firstResult = couponIssueRedisStore.reserve(couponTemplateId, userId, null)

        // when
        val secondResult = couponIssueRedisStore.reserve(couponTemplateId, userId, null)

        // then
        firstResult shouldBe CouponIssueResult.SUCCESS
        secondResult shouldBe CouponIssueResult.ALREADY_ISSUED
    }

    @Test
    fun `쿠폰 발급 시 최대 발급 수를 초과하면 발급이 거부된다`() {
        // given
        val couponTemplateId = 1L
        val userId1 = "user123"
        val userId2 = "user456"
        val maxIssueCount = 1L
        couponIssueRedisStore.initialize(couponTemplateId, null)
        val firstResult = couponIssueRedisStore.reserve(couponTemplateId, userId1, maxIssueCount)

        // when
        val secondResult = couponIssueRedisStore.reserve(couponTemplateId, userId2, maxIssueCount)

        // then
        firstResult shouldBe CouponIssueResult.SUCCESS
        secondResult shouldBe CouponIssueResult.ISSUE_LIMIT_EXCEEDED
    }

    @Test
    fun `쿠폰을 사용할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val issuedKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)
        val redeemedKey = CouponRedisKeyUtils.redeemedKey(couponTemplateId)
        couponIssueRedisStore.initialize(couponTemplateId, null)
        couponIssueRedisStore.reserve(couponTemplateId, userId, null)
        couponIssueRedisStore.complete(couponTemplateId, userId)

        // when
        couponIssueRedisStore.redeem(couponTemplateId, userId, null)

        // then
        redisTemplate.opsForSet().size(issuedKey) shouldBe 2L
        redisTemplate.opsForSet().size(redeemedKey) shouldBe 2L
        redisTemplate.opsForSet().isMember(redeemedKey, userId) shouldBe true
    }

    @Test
    fun `쿠폰을 중복 사용하지 못한다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        couponIssueRedisStore.initialize(couponTemplateId, null)
        couponIssueRedisStore.reserve(couponTemplateId, userId, null)
        couponIssueRedisStore.complete(couponTemplateId, userId)
        couponIssueRedisStore.redeem(couponTemplateId, userId, null)

        // when
        val result = couponIssueRedisStore.redeem(couponTemplateId, userId, null)

        // then
        result shouldBe CouponRedeemResult.ALREADY_REDEEMED
    }

    @Test
    fun `쿠폰 사용 시 최대 사용 수를 초과하면 사용이 거부된다`() {
        // given
        val couponTemplateId = 1L
        val userId1 = "user123"
        val maxRedeemCount = 1L
        couponIssueRedisStore.initialize(couponTemplateId, null)
        couponIssueRedisStore.reserve(couponTemplateId, userId1, null)
        couponIssueRedisStore.complete(couponTemplateId, userId1)
        val firstResult = couponIssueRedisStore.redeem(couponTemplateId, userId1, maxRedeemCount)

        // when
        val userId2 = "user456"
        couponIssueRedisStore.reserve(couponTemplateId, userId2, null)
        couponIssueRedisStore.complete(couponTemplateId, userId2)
        val secondResult = couponIssueRedisStore.redeem(couponTemplateId, userId2, maxRedeemCount)

        // then
        firstResult shouldBe CouponRedeemResult.SUCCESS
        secondResult shouldBe CouponRedeemResult.REDEEM_LIMIT_EXCEEDED
    }

    @Test
    fun `쿠폰 발급을 취소할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val issuedKey = CouponRedisKeyUtils.issuedKey(couponTemplateId)
        couponIssueRedisStore.initialize(couponTemplateId, null)
        couponIssueRedisStore.reserve(couponTemplateId, userId, null)
        couponIssueRedisStore.complete(couponTemplateId, userId)
        redisTemplate.opsForSet().size(issuedKey) shouldBe 2L

        // when
        couponIssueRedisStore.cancelIssue(couponTemplateId, userId)

        // then
        redisTemplate.opsForSet().size(issuedKey) shouldBe 1L
        redisTemplate.opsForSet().isMember(issuedKey, userId) shouldBe false
    }

    @Test
    fun `쿠폰 사용을 취소할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val redeemedKey = CouponRedisKeyUtils.redeemedKey(couponTemplateId)
        couponIssueRedisStore.initialize(couponTemplateId, null)
        couponIssueRedisStore.reserve(couponTemplateId, userId, null)
        couponIssueRedisStore.complete(couponTemplateId, userId)
        couponIssueRedisStore.redeem(couponTemplateId, userId, null)
        redisTemplate.opsForSet().size(redeemedKey) shouldBe 2L

        // when
        couponIssueRedisStore.cancelRedeem(couponTemplateId, userId)

        // then
        redisTemplate.opsForSet().size(redeemedKey) shouldBe 1L
        redisTemplate.opsForSet().isMember(redeemedKey, userId) shouldBe false
    }
}