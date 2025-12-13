package com.goodpon.consumer.couponissuer.application

import com.goodpon.application.couponissuer.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.couponissuer.service.IssueCouponService
import com.goodpon.consumer.couponissuer.support.AbstractIntegrationTest
import com.goodpon.consumer.couponissuer.support.accessor.TestCouponTemplateAccessor
import com.goodpon.consumer.couponissuer.support.accessor.TestMerchantAccessor
import com.goodpon.consumer.couponissuer.support.accessor.TestUserCouponAccessor
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuancePeriodException
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisCommandCache
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisQueryCache
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
    private val commandCache: CouponTemplateStatsRedisCommandCache,
    private val queryCache: CouponTemplateStatsRedisQueryCache,
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
        commandCache.reserveCoupon(
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