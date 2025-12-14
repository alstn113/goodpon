package com.goodpon.couponissuer.bootstrap.application

import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
import com.goodpon.couponissuer.application.service.IssueCouponService
import com.goodpon.couponissuer.bootstrap.support.AbstractIntegrationTest
import com.goodpon.couponissuer.bootstrap.support.accessor.TestCouponTemplateAccessor
import com.goodpon.couponissuer.bootstrap.support.accessor.TestMerchantAccessor
import com.goodpon.couponissuer.bootstrap.support.accessor.TestUserCouponAccessor
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuancePeriodException
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.redis.coupon.CouponIssueRedisStore
import com.goodpon.infra.redis.coupon.CouponStatsRedisStore
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class IssueCouponServiceIT(
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val commandCache: CouponIssueRedisStore,
    private val queryCache: CouponStatsRedisStore,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰을 발급할 수 있다`() {
        // given
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(merchantId = merchantId)
        val userId = "unique-user-id"

        val command = IssueCouponCommand(
            couponTemplateId = couponTemplateId,
            userId = userId,
            requestedAt = LocalDateTime.now()
        )

        // when
        issueCouponService(command)

        // then
        val foundCoupon = testUserCouponAccessor.findAll().first()
        foundCoupon.couponTemplateId shouldBe couponTemplateId
        foundCoupon.userId shouldBe userId
        foundCoupon.status shouldBe UserCouponStatus.ISSUED
    }

    @Test
    fun `이미 쿠폰을 발급한 사용자에게는 중복 발급하지 않고, 무시한다`() {
        // given
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(merchantId = merchantId)
        val userId = "unique-user-id"

        val command = IssueCouponCommand(
            couponTemplateId = couponTemplateId,
            userId = userId,
            requestedAt = LocalDateTime.now()
        )

        shouldNotThrow<Exception> {
            issueCouponService(command)
        }
    }

    @Test
    fun `쿠폰 발급 실패 시 보상 로직(쿠폰 발급 수 감소)이 정상 동작한다`() {
        // given
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            merchantId = merchantId,
            issueStartAt = LocalDateTime.now().plusDays(1),
            issueEndAt = LocalDateTime.now().plusDays(2),
            maxIssueCount = 10

        )
        val userId = "unique-user-id"
        commandCache.reserve(
            couponTemplateId = couponTemplateId,
            userId = userId,
            maxIssueCount = 10
        )
        queryCache.getStats(couponTemplateId).let {
            it.first shouldBe 1L
            it.second shouldBe 0L
        }

        // when
        shouldThrow<CouponTemplateIssuancePeriodException> {
            issueCouponService(
                IssueCouponCommand(
                    couponTemplateId = couponTemplateId,
                    userId = userId,
                    requestedAt = LocalDateTime.now()
                )
            )
        }

        // then
        queryCache.getStats(couponTemplateId).let {
            it.first shouldBe 0L
            it.second shouldBe 0L
        }
    }
}