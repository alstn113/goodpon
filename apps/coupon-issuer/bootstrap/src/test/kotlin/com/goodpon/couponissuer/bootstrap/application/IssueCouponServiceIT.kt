package com.goodpon.couponissuer.bootstrap.application

import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
import com.goodpon.couponissuer.application.service.IssueCouponService
import com.goodpon.couponissuer.bootstrap.support.AbstractIntegrationTest
import com.goodpon.couponissuer.bootstrap.support.accessor.TestCouponTemplateAccessor
import com.goodpon.couponissuer.bootstrap.support.accessor.TestMerchantAccessor
import com.goodpon.couponissuer.bootstrap.support.accessor.TestUserCouponAccessor
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.redis.coupon.CouponIssueRedisStore
import com.goodpon.infra.redis.coupon.CouponRedisKeyUtils
import com.goodpon.infra.redis.coupon.CouponStatsRedisStore
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDateTime

class IssueCouponServiceIT(
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val couponIssueStore: CouponIssueRedisStore,
    private val couponStatsStore: CouponStatsRedisStore,
    private val redisTemplate: RedisTemplate<String, String>,
) : AbstractIntegrationTest() {

    private var merchantId: Long = 0L
    private var couponTemplateId: Long = 0L
    private val userId = "unique-user-id"

    @BeforeEach
    fun setUp() {
        merchantId = testMerchantAccessor.createMerchant().first
        couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            merchantId = merchantId,
            maxIssueCount = 100
        )
    }

    @Test
    fun `예약 정보가 있고 조건이 맞으면 쿠폰이 발급되고 저장된다`() {
        // given
        couponIssueStore.reserve(couponTemplateId, userId, 100)

        val stats = couponStatsStore.getStats(couponTemplateId)
        stats.first shouldBe 1L // 예약된 상태

        val command = IssueCouponCommand(
            couponTemplateId = couponTemplateId,
            userId = userId,
            requestedAt = LocalDateTime.now()
        )

        // when
        issueCouponService(command)

        // then
        val userCoupons = testUserCouponAccessor.findAll()
        userCoupons.size shouldBe 1

        val issuedCoupon = userCoupons.first()
        issuedCoupon.couponTemplateId shouldBe couponTemplateId
        issuedCoupon.userId shouldBe userId
        issuedCoupon.status shouldBe UserCouponStatus.ISSUED
        issuedCoupon.issuedAt shouldNotBe null

        reservedAndIssuedShouldBe(
            couponTemplateId = couponTemplateId,
            reserved = 0L,
            issued = 1L,
        )
    }

    @Test
    fun `이미 발급된 쿠폰이 있으면 새로 생성하지 않는다`() {
        // given
        couponIssueStore.reserve(couponTemplateId, userId, 100)
        issueCouponService(IssueCouponCommand(couponTemplateId, userId, LocalDateTime.now()))

        testUserCouponAccessor.findAll().size shouldBe 1

        // when
        // 동일한 사용자가 다시 쿠폰 발급 요청 (불가능한 상황)
        couponIssueStore.reserve(couponTemplateId, userId, 100)
        issueCouponService(IssueCouponCommand(couponTemplateId, userId, LocalDateTime.now()))

        // then
        testUserCouponAccessor.findAll().size shouldBe 1
    }

    private fun reservedAndIssuedShouldBe(couponTemplateId: Long, reserved: Long, issued: Long) {
        val reservedSize = redisTemplate.opsForZSet()
            .size(CouponRedisKeyUtils.reservedKey(couponTemplateId))
        val issuedSize = redisTemplate.opsForSet()
            .size(CouponRedisKeyUtils.issuedKey(couponTemplateId))

        reservedSize shouldBe reserved + 1 // dummy
        issuedSize shouldBe issued + 1 // dummy
    }
}