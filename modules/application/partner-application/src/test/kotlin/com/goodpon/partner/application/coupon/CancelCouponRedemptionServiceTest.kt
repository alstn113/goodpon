package com.goodpon.partner.application.coupon

import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.partner.application.coupon.service.CancelCouponRedemptionService
import com.goodpon.partner.application.coupon.service.CouponRedemptionCancelProcessor
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateStatsAccessor
import com.goodpon.partner.application.coupon.service.accessor.UserCouponAccessor
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class CancelCouponRedemptionServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val couponTemplateStatsAccessor = mockk<CouponTemplateStatsAccessor>()
    val userCouponAccessor = mockk<UserCouponAccessor>()
    val couponRedemptionCancelProcessor = mockk<CouponRedemptionCancelProcessor>()
    val cancelCouponRedemptionService = CancelCouponRedemptionService(
        couponTemplateAccessor = couponTemplateAccessor,
        couponTemplateStatsAccessor = couponTemplateStatsAccessor,
        userCouponAccessor = userCouponAccessor,
        couponRedemptionCancelProcessor = couponRedemptionCancelProcessor
    )

    describe("CancelCouponRedemptionService") {
        val command = CancelCouponRedemptionCommand(
            userCouponId = "unique-user-coupon-id",
            orderId = "unique-order-id",
            merchantId = 1L,
            cancelReason = "결제 취소"
        )

        val userCoupon = mockk<UserCoupon>()
        val stats = mockk<CouponTemplateStats>()
        val couponTemplate = mockk<CouponTemplate>()

        beforeTest {
            every { userCouponAccessor.readByIdForUpdate(any()) } returns userCoupon
            every { userCoupon.couponTemplateId } returns 1L
            every { couponTemplateStatsAccessor.readByCouponTemplateIdForUpdate(any()) } returns stats
            every { couponTemplateAccessor.readById(any()) } returns couponTemplate
        }

        it("상점이 소유한 쿠폰이 아닌 경우 예외를 던진다.") {
            every { couponTemplate.isOwnedBy(command.merchantId) } returns false

            shouldThrow<CouponTemplateNotOwnedByMerchantException> {
                cancelCouponRedemptionService(command)
            }
        }
    }
})