package com.goodpon.api.partner.application.coupon

import com.goodpon.api.partner.support.AbstractIntegrationTest
import com.goodpon.api.partner.support.FakeCouponEventPublisher
import com.goodpon.api.partner.support.accessor.TestCouponTemplateAccessor
import com.goodpon.api.partner.support.accessor.TestMerchantAccessor
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.service.IssueCouponService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class IssueCouponServiceIT(
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
    private val couponEventPublisher: FakeCouponEventPublisher,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰 발급 요청 시 발급 수량이 증가한다`() {
        // given
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(merchantId = merchantId)
        val userId = "unique-user-id"

        // when
        issueCouponService(
            IssueCouponCommand(
                merchantId = merchantId,
                couponTemplateId = couponTemplateId,
                userId = userId,
            )
        )

        // then
        couponTemplateStatsCache.getStats(couponTemplateId).let {
            it.first shouldBe 1L
            it.second shouldBe 0L
        }
        val event = couponEventPublisher.consume()
        event?.couponTemplateId shouldBe couponTemplateId
        event?.userId shouldBe userId
    }
}
