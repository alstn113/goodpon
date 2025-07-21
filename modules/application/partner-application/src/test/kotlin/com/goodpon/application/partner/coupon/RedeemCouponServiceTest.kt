package com.goodpon.application.partner.coupon

import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.application.partner.coupon.service.RedeemCouponService
import com.goodpon.application.partner.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateStatsAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponNotOwnedByUserException
import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class RedeemCouponServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val couponTemplateStatsAccessor = mockk<CouponTemplateStatsAccessor>()
    val userCouponAccessor = mockk<UserCouponAccessor>()
    val couponHistoryAccessor = mockk<CouponHistoryAccessor>()

    val redeemCouponService = RedeemCouponService(
        couponTemplateAccessor = couponTemplateAccessor,
        couponTemplateStatsAccessor = couponTemplateStatsAccessor,
        userCouponAccessor = userCouponAccessor,
        couponHistoryAccessor = couponHistoryAccessor
    )

    describe("RedeemCouponService 예외 처리 테스트") {
        val command = RedeemCouponCommand(
            userCouponId = "unique-user-coupon-id",
            userId = "unique-user-id",
            merchantId = 1L,
            orderAmount = 15000,
            orderId = "unique-order-id"
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

        it("상점이 소유한 쿠폰 템플릿이 아닌 경우 예외를 던진다") {
            every { couponTemplate.isOwnedBy(command.merchantId) } returns false

            shouldThrow<CouponTemplateNotOwnedByMerchantException> {
                redeemCouponService.invoke(command)
            }
        }

        it("사용자가 소유한 쿠폰이 아닌 경우 예외를 던진다") {
            every { couponTemplate.isOwnedBy(command.merchantId) } returns true
            every { userCoupon.isOwnedBy(command.userId) } returns false

            shouldThrow<UserCouponNotOwnedByUserException> {
                redeemCouponService.invoke(command)
            }
        }
    }
})