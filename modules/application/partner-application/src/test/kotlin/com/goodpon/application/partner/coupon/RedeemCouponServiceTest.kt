package com.goodpon.application.partner.coupon

import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.port.out.dto.RedeemResult
import com.goodpon.application.partner.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.application.partner.coupon.service.RedeemCouponService
import com.goodpon.application.partner.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponNotFoundException
import com.goodpon.application.partner.coupon.service.exception.UserCouponNotOwnedByUserException
import com.goodpon.domain.coupon.template.CouponTemplateFactory
import com.goodpon.domain.coupon.template.exception.CouponTemplateRedemptionLimitExceededException
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.exception.UserCouponAlreadyRedeemedException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDate
import java.time.LocalDateTime

class RedeemCouponServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val userCouponAccessor = mockk<UserCouponAccessor>()
    val couponHistoryAccessor = mockk<CouponHistoryAccessor>()
    val couponTemplateStatsCache = mockk<CouponTemplateStatsCache>()
    val couponEventPublisher = mockk<ApplicationEventPublisher>()

    val redeemCouponService = RedeemCouponService(
        couponTemplateAccessor = couponTemplateAccessor,
        userCouponAccessor = userCouponAccessor,
        couponHistoryAccessor = couponHistoryAccessor,
        couponTemplateStatsCache = couponTemplateStatsCache,
        eventPublisher = couponEventPublisher
    )

    describe("RedeemCouponService") {
        val command = RedeemCouponCommand(
            userCouponId = "unique-user-coupon-id",
            userId = "unique-user-id",
            merchantId = 1L,
            orderAmount = 15000,
            orderId = "unique-order-id"
        )
        val couponTemplateId = 1L
        val couponTemplate = CouponTemplateFactory.create(
            merchantId = command.merchantId,
            name = "Test Coupon",
            description = "Test Description",
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 5000,
            maxDiscountAmount = null,
            issueStartDate = LocalDate.now(),
            issueEndDate = null,
            validityDays = null,
            absoluteExpiryDate = null,
            limitType = CouponLimitPolicyType.NONE,
            maxIssueCount = null,
            maxRedeemCount = null,
            status = CouponTemplateStatus.ISSUABLE
        ).copy(id = couponTemplateId)
        val userCoupon = UserCoupon.issue(
            userId = command.userId,
            couponTemplateId = couponTemplate.id,
            expiresAt = null,
            issueAt = LocalDateTime.now()
        )

        beforeTest {
            every { userCouponAccessor.readByIdForUpdate(any()) } returns userCoupon
            every { couponTemplateAccessor.readById(any()) } returns couponTemplate
            every { couponTemplateStatsCache.redeemCoupon(any(), any(), any()) } returns RedeemResult.SUCCESS
            every { userCouponAccessor.update(any()) } returns userCoupon
            every { couponHistoryAccessor.recordRedeemed(any(), any(), any()) } returns mockk()
        }

        it("존재하지 않는 사용자 쿠폰인 경우 예외를 던진다.") {
            every { userCouponAccessor.readByIdForUpdate(command.userCouponId) } throws UserCouponNotFoundException()

            shouldThrow<UserCouponNotFoundException> {
                redeemCouponService.invoke(command)
            }
        }

        it("존재하지 않는 쿠폰 템플릿인 경우 예외를 던진다.") {
            every { couponTemplateAccessor.readById(any()) } throws CouponTemplateNotFoundException()

            shouldThrow<CouponTemplateNotFoundException> {
                redeemCouponService.invoke(command)
            }
        }

        it("상점이 소유한 쿠폰 템플릿이 아닌 경우 예외를 던진다.") {
            every {
                couponTemplateAccessor.readById(couponTemplateId)
            } returns couponTemplate.copy(merchantId = -1L)

            shouldThrow<CouponTemplateNotOwnedByMerchantException> {
                redeemCouponService.invoke(command)
            }
        }

        it("사용자가 소유한 쿠폰이 아닌 경우 예외를 던진다.") {
            every {
                userCouponAccessor.readByIdForUpdate(any())
            } returns userCoupon.copy(userId = "another-user-id")

            shouldThrow<UserCouponNotOwnedByUserException> {
                redeemCouponService.invoke(command)
            }
        }

        it("이미 사용한 쿠폰인 경우 예외를 던진다.") {
            every {
                couponTemplateStatsCache.redeemCoupon(any(), any(), any())
            } returns RedeemResult.ALREADY_REDEEMED

            shouldThrow<UserCouponAlreadyRedeemedException> {
                redeemCouponService.invoke(command)
            }

            verify(exactly = 0) { couponEventPublisher.publishEvent(any()) }
        }

        it("쿠폰 사용 제한을 초과한 경우 예외를 던진다.") {
            every {
                couponTemplateStatsCache.redeemCoupon(any(), any(), any())
            } returns RedeemResult.REDEEM_LIMIT_EXCEEDED

            shouldThrow<CouponTemplateRedemptionLimitExceededException> {
                redeemCouponService.invoke(command)
            }

            verify(exactly = 0) { couponEventPublisher.publishEvent(any()) }
        }
    }
})
