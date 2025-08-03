package com.goodpon.infra.redis.coupon

import com.goodpon.infra.redis.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.RedisTemplate

class CouponTemplateStatsRedisCacheAdapterIT(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val couponTemplateStatsRedisCacheAdapter: CouponTemplateStatsRedisCacheAdapter,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰 템플릿 통계 정보를 초기화할 수 있다`() {
        // given
        val couponTemplateId = 1L
        val expiresAt = null // 만료 시간 없음

        // when
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, expiresAt)

        // then
        redisTemplate.opsForHash<String, Any>().hasKey("coupon-template-stats:1", "issueCount") shouldBe true
        redisTemplate.opsForHash<String, Any>().hasKey("coupon-template-stats:1", "redeemCount") shouldBe true
    }

    @Test
    fun `쿠폰 템플릿 발행 카운트를 증가시킬 수 있다`() {
        // given
        val couponTemplateId = 1L
        val limit = 10L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)

        // when
        val result = couponTemplateStatsRedisCacheAdapter.incrementIssueCount(couponTemplateId, limit)

        // then
        result shouldBe true
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)
        stats.first shouldBe 1L
        stats.second shouldBe 0L
    }

    @Test
    fun `쿠폰 템플릿 발행 카운트를 증가시킬 때 제한을 초과하면 false를 반환한다`() {
        // given
        val couponTemplateId = 1L
        val limit = 1L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)

        // when
        couponTemplateStatsRedisCacheAdapter.incrementIssueCount(couponTemplateId, limit)
        val result = couponTemplateStatsRedisCacheAdapter.incrementIssueCount(couponTemplateId, limit)

        // then
        result shouldBe false
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)
        stats.first shouldBe 1L
    }

    @Test
    fun `쿠폰 템플릿 발행 카운트를 취소할 수 있다`() {
        // given
        val couponTemplateId = 1L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)
        couponTemplateStatsRedisCacheAdapter.incrementIssueCount(couponTemplateId, null)

        // when
        val result = couponTemplateStatsRedisCacheAdapter.cancelIssue(couponTemplateId)

        // then
        result shouldBe true
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)
        stats.first shouldBe 0L
    }

    @Test
    fun `쿠폰 템플릿 발행 카운트를 취소할 때 통계가 없으면 false를 반환한다`() {
        // given
        val couponTemplateId = 1L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)

        // when
        val result = couponTemplateStatsRedisCacheAdapter.cancelIssue(couponTemplateId)

        // then
        result shouldBe false
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)
        stats.first shouldBe 0L
    }

    @Test
    fun `쿠폰 템플릿 사용 카운트를 증가시킬 수 있다`() {
        // given
        val couponTemplateId = 1L
        val limit = 10L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)

        // when
        val result = couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(couponTemplateId, limit)

        // then
        result shouldBe true
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)
        stats.second shouldBe 1L
    }

    @Test
    fun `쿠폰 템플릿 사용 카운트를 증가시킬 때 제한을 초과하면 false를 반환한다`() {
        // given
        val couponTemplateId = 1L
        val limit = 1L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)

        // when
        couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(couponTemplateId, limit)
        val result = couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(couponTemplateId, limit)

        // then
        result shouldBe false
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)
        stats.second shouldBe 1L
    }

    @Test
    fun `쿠폰 템플릿 사용 카운트를 취소할 수 있다`() {
        // given
        val couponTemplateId = 1L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)
        couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(couponTemplateId, null)

        // when
        val result = couponTemplateStatsRedisCacheAdapter.cancelRedeem(couponTemplateId)

        // then
        result shouldBe true
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)
        stats.second shouldBe 0L
    }

    @Test
    fun `쿠폰 템플릿 사용 카운트를 취소할 때 통계가 없으면 false를 반환한다`() {
        // given
        val couponTemplateId = 1L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)

        // when
        val result = couponTemplateStatsRedisCacheAdapter.cancelRedeem(couponTemplateId)

        // then
        result shouldBe false
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)
        stats.second shouldBe 0L
    }

    @Test
    fun `쿠폰 템플릿 통계 정보를 조회할 수 있다`() {
        // given
        val couponTemplateId = 1L
        couponTemplateStatsRedisCacheAdapter.initializeStats(couponTemplateId, null)
        couponTemplateStatsRedisCacheAdapter.incrementIssueCount(couponTemplateId, null)
        couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(couponTemplateId, null)

        // when
        val stats = couponTemplateStatsRedisCacheAdapter.getStats(couponTemplateId)

        // then
        stats.first shouldBe 1L // 발행 카운트
        stats.second shouldBe 1L // 사용 카운트
    }

    @Test
    fun `여러 쿠폰 템플릿의 통계 정보를 조회할 수 있다`() {
        // given
        val couponTemplateIds = listOf(1L, 2L, 3L)
        couponTemplateIds.forEach { couponTemplateStatsRedisCacheAdapter.initializeStats(it, null) }

        // 1 - 1,2
        couponTemplateStatsRedisCacheAdapter.incrementIssueCount(1L, null)
        couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(1L, null)
        couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(1L, null)

        // 2 - 0,1
        couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(2L, null)

        // 3 - 1,1
        couponTemplateStatsRedisCacheAdapter.incrementIssueCount(3L, null)
        couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(3L, null)

        // when
        val statsMap = couponTemplateStatsRedisCacheAdapter.getMultipleStats(couponTemplateIds)

        // then
        statsMap.size shouldBe 3
        statsMap[1L] shouldBe Pair(1L, 2L)
        statsMap[2L] shouldBe Pair(0L, 1L)
        statsMap[3L] shouldBe Pair(1L, 1L)
    }

    @Test
    fun `존재하는 모든 쿠폰 템플릿의 통계 정보를 조회할 수 있다`() {
        // given
        val couponTemplateIds = listOf(1L, 2L, 3L)
        couponTemplateIds.forEach { couponTemplateStatsRedisCacheAdapter.initializeStats(it, null) }

        couponTemplateStatsRedisCacheAdapter.incrementIssueCount(2L, null)
        couponTemplateStatsRedisCacheAdapter.incrementRedeemCount(3L, null)

        // when
        val statsMap = couponTemplateStatsRedisCacheAdapter.readAllStats()

        // then
        statsMap.size shouldBe 3
        statsMap[1L] shouldBe Pair(0L, 0L)
        statsMap[2L] shouldBe Pair(1L, 0L)
        statsMap[3L] shouldBe Pair(0L, 1L)
    }
}