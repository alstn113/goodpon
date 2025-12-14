package com.goodpon.partner.bootstrap.application.coupon

import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.out.CouponStatsStore
import com.goodpon.partner.application.coupon.service.IssueCouponService
import com.goodpon.partner.bootstrap.support.AbstractIntegrationTest
import com.goodpon.partner.bootstrap.support.FakeCouponEventPublisher
import com.goodpon.partner.bootstrap.support.accessor.TestCouponTemplateAccessor
import com.goodpon.partner.bootstrap.support.accessor.TestMerchantAccessor
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class IssueCouponServiceIT(
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val couponStatsStore: CouponStatsStore,
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
        couponStatsStore.getStats(couponTemplateId).let {
            it.first shouldBe 1L
            it.second shouldBe 0L
        }
        val event = couponEventPublisher.consume()
        event?.couponTemplateId shouldBe couponTemplateId
        event?.userId shouldBe userId
    }
}
