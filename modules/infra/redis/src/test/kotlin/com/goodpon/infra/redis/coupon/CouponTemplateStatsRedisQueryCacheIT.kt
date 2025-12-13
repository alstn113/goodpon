package com.goodpon.infra.redis.coupon

import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisCommandCache
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisQueryCache
import com.goodpon.infra.redis.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CouponTemplateStatsRedisQueryCacheIT(
    private val commandCache: CouponTemplateStatsRedisCommandCache,
    private val queryCache: CouponTemplateStatsRedisQueryCache,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰 템플릿 통계 초기 정보를 조회할 수 있다`() {
        // given
        val couponTemplateId = 1L
        commandCache.initializeStats(couponTemplateId, null)

        // when
        val stats = queryCache.getStats(couponTemplateId)

        // then
        stats.first shouldBe 0L
        stats.second shouldBe 0L
    }

    @Test
    fun `쿠폰 템플릿 통계 정보를 조회할 수 있다`() {
        // given
        val couponTemplateId = 1L
        commandCache.initializeStats(couponTemplateId, null)
        commandCache.reserveCoupon(couponTemplateId, "user1", null)
        commandCache.reserveCoupon(couponTemplateId, "user2", null)
        commandCache.redeemCoupon(couponTemplateId, "user1", null)

        // when
        val stats = queryCache.getStats(couponTemplateId)

        // then
        stats.first shouldBe 2L
        stats.second shouldBe 1L
    }

    @Test
    fun `여러 쿠폰 템플릿 식별자들에 대해서 통계 정보를 조회할 수 있다`() {
        // given
        initializeStats()

        // when
        val stats = queryCache.getMultipleStats(listOf(1L, 3L))

        // then
        stats.size shouldBe 2
        stats[1L] shouldBe Pair(2L, 1L)
        stats[3L] shouldBe Pair(1L, 1L)
    }

    @Test
    fun `존재하는 모든 쿠폰 템플릿 통계 정보에 대해서 알 수 있다`() {
        // given
        initializeStats()

        // when
        val stats = queryCache.readAllStats()

        // then
        stats.size shouldBe 4
        stats[1L] shouldBe Pair(2L, 1L)
        stats[2L] shouldBe Pair(0L, 0L)
        stats[3L] shouldBe Pair(1L, 1L)
        stats[4L] shouldBe Pair(1L, 0L)
    }

    private fun initializeStats() {
        val couponTemplateId1 = 1L // issueCount: 2, redeemCount: 1
        commandCache.initializeStats(couponTemplateId1, null)
        commandCache.reserveCoupon(couponTemplateId1, "user1", null)
        commandCache.redeemCoupon(couponTemplateId1, "user1", null)
        commandCache.reserveCoupon(couponTemplateId1, "user2", null)

        val couponTemplateId2 = 2L
        commandCache.initializeStats(couponTemplateId2, null) // issueCount: 0, redeemCount: 0

        val couponTemplateId3 = 3L // issueCount: 1, redeemCount: 1
        commandCache.initializeStats(couponTemplateId3, null)
        commandCache.reserveCoupon(couponTemplateId3, "user4", null)
        commandCache.redeemCoupon(couponTemplateId3, "user4", null)

        val couponTemplateId4 = 4L // issueCount: 1, redeemCount: 0
        commandCache.initializeStats(couponTemplateId4, null)
        commandCache.reserveCoupon(couponTemplateId4, "user5", null)
    }
}