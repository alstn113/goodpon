package com.goodpon.infra.redis.coupon

import com.goodpon.infra.redis.support.AbstractIntegrationTest
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.Duration
import kotlin.test.Test

class CouponStatsRedisStoreIT(
    private val couponIssueStore: CouponIssueRedisStore,
    private val couponStatsStore: CouponStatsRedisStore,
    private val redisTemplate: StringRedisTemplate,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰 템플릿 통계 초기 정보를 조회할 수 있다`() {
        // given
        val couponTemplateId = 1L
        couponIssueStore.initialize(couponTemplateId, null)

        // when
        val stats = couponStatsStore.getStats(couponTemplateId)

        // then
        stats.first shouldBe 0L
        stats.second shouldBe 0L
    }

    @Test
    fun `쿠폰 템플릿 통계 정보를 조회할 수 있다`() {
        // given
        val couponTemplateId = 1L
        couponIssueStore.initialize(couponTemplateId, null)
        couponIssueStore.reserve(couponTemplateId, "user1", null)
        couponIssueStore.reserve(couponTemplateId, "user2", null)
        couponIssueStore.redeem(couponTemplateId, "user1", null)

        // when
        val stats = couponStatsStore.getStats(couponTemplateId)

        // then
        stats.first shouldBe 2L // issued + reserved
        stats.second shouldBe 1L // redeemed
    }

    @Test
    fun `여러 쿠폰 템플릿 식별자들에 대해서 통계 정보를 조회할 수 있다`() {
        // given
        initializeStats()

        // when
        val stats = couponStatsStore.getMultipleStats(listOf(1L, 3L))

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
        val stats = couponStatsStore.getAllStats()

        // then
        stats.size shouldBe 4
        stats[1L] shouldBe Pair(2L, 1L)
        stats[2L] shouldBe Pair(0L, 0L)
        stats[3L] shouldBe Pair(1L, 1L)
        stats[4L] shouldBe Pair(1L, 0L)
    }

    @Test
    fun `오래된(Stale) 쿠폰 발급 예약 정보를 조회할 수 있다`() {
        // given
        val couponTemplateId = 99L
        val staleUserId = "stale-user"
        val recentUserId = "recent-user"
        val olderThan = Duration.ofMinutes(5)

        couponIssueStore.initialize(couponTemplateId, null)

        // 10분 전 발급 예약된 데이터 생성
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        val now = System.currentTimeMillis()
        val pastTime = now - Duration.ofMinutes(10).toMillis()
        redisTemplate.opsForZSet().add(reservedKey, staleUserId, pastTime.toDouble())

        // 최신 데이터 생성
        redisTemplate.opsForZSet().add(reservedKey, recentUserId, now.toDouble())

        // when
        val staleReservations = couponStatsStore.getAllStaleCouponIssueReservations(olderThan)

        // then
        staleReservations.containsKey(couponTemplateId) shouldBe true
        staleReservations[couponTemplateId]?.shouldContainExactlyInAnyOrder(staleUserId)
        staleReservations[couponTemplateId]?.contains(recentUserId) shouldBe false
    }

    @Test
    fun `쿠폰 발급 예약에 대해서 실패 마킹한 내역은 오래된 예약으로 간주되지 않는다`() {
        // given
        val couponTemplateId = 100L
        val failedUserId = "failed-user"
        val olderThan = Duration.ofMinutes(5)

        couponIssueStore.initialize(couponTemplateId, null)

        // 10분 전 발급 예약된 데이터 생성
        val reservedKey = CouponRedisKeyUtils.reservedKey(couponTemplateId)
        val now = System.currentTimeMillis()
        val pastTime = now - Duration.ofMinutes(10).toMillis()
        redisTemplate.opsForZSet().add(reservedKey, failedUserId, pastTime.toDouble())

        // 실패로 마킹
        couponIssueStore.markAsFailed(couponTemplateId, failedUserId)

        // when
        val staleReservations = couponStatsStore.getAllStaleCouponIssueReservations(olderThan)

        // then
        staleReservations.containsKey(couponTemplateId) shouldBe false
    }

    private fun initializeStats() {
        val couponTemplateId1 = 1L
        couponIssueStore.initialize(couponTemplateId1, null)
        couponIssueStore.reserve(couponTemplateId1, "user1", null)
        couponIssueStore.redeem(couponTemplateId1, "user1", null)
        couponIssueStore.reserve(couponTemplateId1, "user2", null)

        val couponTemplateId2 = 2L
        couponIssueStore.initialize(couponTemplateId2, null)

        val couponTemplateId3 = 3L
        couponIssueStore.initialize(couponTemplateId3, null)
        couponIssueStore.reserve(couponTemplateId3, "user4", null)
        couponIssueStore.redeem(couponTemplateId3, "user4", null)

        val couponTemplateId4 = 4L
        couponIssueStore.initialize(couponTemplateId4, null)
        couponIssueStore.reserve(couponTemplateId4, "user5", null)
    }
}