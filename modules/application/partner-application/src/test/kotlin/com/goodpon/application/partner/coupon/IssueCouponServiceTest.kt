package com.goodpon.application.partner.coupon

import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.service.IssueCouponService
import com.goodpon.application.partner.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateStatsAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.domain.coupon.template.CouponTemplate
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class IssueCouponServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val couponTemplateStatsAccessor = mockk<CouponTemplateStatsAccessor>()
    val couponHistoryAccessor = mockk<CouponHistoryAccessor>()
    val userCouponAccessor = mockk<UserCouponAccessor>()

    val issueCouponService = IssueCouponService(
        couponTemplateAccessor = couponTemplateAccessor,
        couponTemplateStatsAccessor = couponTemplateStatsAccessor,
        couponHistoryAccessor = couponHistoryAccessor,
        userCouponAccessor = userCouponAccessor,
    )

    describe("IssueCouponService") {

        val command = IssueCouponCommand(
            couponTemplateId = 1L,
            userId = "testUser123",
            merchantId = 100L
        )

        it("상점이 소유하지 않은 쿠폰 템플릿인 경우 예외를 던진다") {
            every { couponTemplateStatsAccessor.readByCouponTemplateIdForUpdate(command.couponTemplateId) } returns mockk()
            val couponTemplate = mockk<CouponTemplate>()
            every { couponTemplateAccessor.readById(command.couponTemplateId) } returns couponTemplate
            every { couponTemplate.isOwnedBy(command.merchantId) } returns false

            shouldThrow<CouponTemplateNotOwnedByMerchantException> {
                issueCouponService(command)
            }
        }

        it("사용자가 이미 발급받은 쿠폰인 경우 예외를 던진다") {
            every { couponTemplateStatsAccessor.readByCouponTemplateIdForUpdate(command.couponTemplateId) } returns mockk()
            val couponTemplate = mockk<CouponTemplate>()
            every { couponTemplateAccessor.readById(command.couponTemplateId) } returns couponTemplate
            every { couponTemplate.isOwnedBy(command.merchantId) } returns true
            every {
                userCouponAccessor.existsByUserIdAndCouponTemplateId(command.userId, command.couponTemplateId)
            } returns true

            shouldThrow<UserCouponAlreadyIssuedException> {
                issueCouponService(command)
            }
        }
    }
})