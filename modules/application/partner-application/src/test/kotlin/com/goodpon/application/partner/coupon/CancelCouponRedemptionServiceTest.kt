package com.goodpon.application.partner.coupon

import com.goodpon.application.partner.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.service.CancelCouponRedemptionService
import com.goodpon.application.partner.coupon.service.CouponRedemptionCancelProcessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class CancelCouponRedemptionServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val couponTemplateStatsCache = mockk<CouponTemplateStatsCache>()
    val userCouponAccessor = mockk<UserCouponAccessor>()
    val couponRedemptionCancelProcessor = mockk<CouponRedemptionCancelProcessor>()
    val cancelCouponRedemptionService = CancelCouponRedemptionService(
        couponTemplateAccessor = couponTemplateAccessor,
        couponTemplateStatsCache = couponTemplateStatsCache,
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
        val couponTemplate = mockk<CouponTemplate>()

        beforeTest {
            every { userCouponAccessor.readByIdForUpdate(any()) } returns userCoupon
            every { userCoupon.couponTemplateId } returns 1L
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