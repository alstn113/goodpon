package com.goodpon.infra.redis.coupon

import com.goodpon.application.partner.coupon.port.out.dto.IssueResult
import com.goodpon.application.partner.coupon.port.out.dto.RedeemResult
import com.goodpon.infra.redis.coupon.core.CouponTemplateRedisKeyUtil
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisCommandCache
import com.goodpon.infra.redis.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.LocalDateTime
import kotlin.test.Test

class CouponTemplateStatsRedisCommandCacheIT(
    private val commandCache: CouponTemplateStatsRedisCommandCache,
    private val redisTemplate: StringRedisTemplate,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰 템플릿 발급과 사용 집합을 초기화할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val expiresAt = LocalDateTime.now().plusDays(1)

        // when
        commandCache.initializeStats(couponTemplateId, expiresAt)

        // then
        val issueSetKey = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)
        redisTemplate.hasKey(issueSetKey) shouldBe true
        redisTemplate.opsForSet().size(issueSetKey) shouldBe 1L

        val redeemSetKey = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)
        redisTemplate.hasKey(redeemSetKey) shouldBe true
        redisTemplate.opsForSet().size(redeemSetKey) shouldBe 1L
    }

    @Test
    fun `만료 시간이 없는 경우에도 쿠폰 템플릿 발급과 사용 집합을 초기화할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val expiresAt = null

        // when
        commandCache.initializeStats(couponTemplateId, expiresAt)

        // then
        val issueSetKey = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)
        redisTemplate.hasKey(issueSetKey) shouldBe true
        redisTemplate.opsForSet().size(issueSetKey) shouldBe 1L

        val redeemSetKey = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)
        redisTemplate.hasKey(redeemSetKey) shouldBe true
        redisTemplate.opsForSet().size(redeemSetKey) shouldBe 1L
    }

    @Test
    fun `쿠폰 발급을 추가할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val issueSetKey = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)

        commandCache.initializeStats(couponTemplateId, null)

        // when
        val issueResult = commandCache.issueCoupon(couponTemplateId, userId, null)

        // then
        issueResult shouldBe IssueResult.SUCCESS
        redisTemplate.opsForSet().size(issueSetKey) shouldBe 2L
        redisTemplate.opsForSet().isMember(issueSetKey, userId) shouldBe true
    }

    @Test
    fun `쿠폰 발급 시 중복 발급을 방지할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val issueSetKey = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)

        commandCache.initializeStats(couponTemplateId, null)
        commandCache.issueCoupon(couponTemplateId, userId, null)

        // when
        val issueResult = commandCache.issueCoupon(couponTemplateId, userId, null)

        // then
        issueResult shouldBe IssueResult.ALREADY_ISSUED
        redisTemplate.opsForSet().size(issueSetKey) shouldBe 2L
        redisTemplate.opsForSet().isMember(issueSetKey, userId) shouldBe true
    }

    @Test
    fun `쿠폰 발급 제한을 초과하면 발급 실패를 반환한다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val issueSetKey = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)
        val maxIssueCount = 1L
        commandCache.initializeStats(couponTemplateId, null)
        commandCache.issueCoupon(couponTemplateId, userId, maxIssueCount)

        // when
        val issueResult = commandCache.issueCoupon(couponTemplateId, "user456", maxIssueCount)

        // then
        issueResult shouldBe IssueResult.ISSUE_LIMIT_EXCEEDED
        redisTemplate.opsForSet().size(issueSetKey) shouldBe 2L
        redisTemplate.opsForSet().isMember(issueSetKey, userId) shouldBe true
    }

    @Test
    fun `쿠폰 사용을 추가할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val redeemSetKey = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)

        commandCache.initializeStats(couponTemplateId, null)
        commandCache.issueCoupon(couponTemplateId, userId, null)

        // when
        val redeemResult = commandCache.redeemCoupon(couponTemplateId, userId, null)

        // then
        redeemResult shouldBe RedeemResult.SUCCESS
        redisTemplate.opsForSet().size(redeemSetKey) shouldBe 2L
        redisTemplate.opsForSet().isMember(redeemSetKey, userId) shouldBe true
    }

    @Test
    fun `쿠폰 사용 시 중복 사용을 방지할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val redeemSetKey = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)

        commandCache.initializeStats(couponTemplateId, null)
        commandCache.issueCoupon(couponTemplateId, userId, null)
        commandCache.redeemCoupon(couponTemplateId, userId, null)

        // when
        val redeemResult = commandCache.redeemCoupon(couponTemplateId, userId, null)

        // then
        redeemResult shouldBe RedeemResult.ALREADY_REDEEMED
        redisTemplate.opsForSet().size(redeemSetKey) shouldBe 2L
        redisTemplate.opsForSet().isMember(redeemSetKey, userId) shouldBe true
    }

    @Test
    fun `쿠폰 사용 제한을 초과하면 사용 실패를 반환한다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val redeemSetKey = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)
        val maxRedeemCount = 1L

        commandCache.initializeStats(couponTemplateId, null)
        commandCache.issueCoupon(couponTemplateId, userId, null)
        commandCache.redeemCoupon(couponTemplateId, userId, maxRedeemCount)

        // when
        val redeemResult = commandCache.redeemCoupon(couponTemplateId, "user456", maxRedeemCount)

        // then
        redeemResult shouldBe RedeemResult.REDEEM_LIMIT_EXCEEDED
        redisTemplate.opsForSet().size(redeemSetKey) shouldBe 2L
        redisTemplate.opsForSet().isMember(redeemSetKey, userId) shouldBe true
    }

    @Test
    fun `쿠폰 발급을 취소할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val issueSetKey = CouponTemplateRedisKeyUtil.buildIssueSetKey(couponTemplateId)
        commandCache.initializeStats(couponTemplateId, null)
        commandCache.issueCoupon(couponTemplateId, userId, null)

        // when
        commandCache.cancelIssue(couponTemplateId, userId)

        // then
        redisTemplate.opsForSet().size(issueSetKey) shouldBe 1L
        redisTemplate.opsForSet().isMember(issueSetKey, userId) shouldBe false
    }

    @Test
    fun `쿠폰 사용을 취소할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val userId = "user123"
        val redeemSetKey = CouponTemplateRedisKeyUtil.buildRedeemSetKey(couponTemplateId)

        commandCache.initializeStats(couponTemplateId, null)
        commandCache.redeemCoupon(couponTemplateId, userId, null)

        // when
        commandCache.cancelRedeem(couponTemplateId, userId)

        // then
        redisTemplate.opsForSet().size(redeemSetKey) shouldBe 1L
        redisTemplate.opsForSet().isMember(redeemSetKey, userId) shouldBe false
    }
}