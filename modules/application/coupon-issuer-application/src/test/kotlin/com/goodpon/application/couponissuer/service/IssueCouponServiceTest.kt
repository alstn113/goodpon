package com.goodpon.application.couponissuer.service

import com.goodpon.application.couponissuer.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.couponissuer.service.accessor.CouponHistoryAccessor
import com.goodpon.application.couponissuer.service.accessor.CouponTemplateAccessor
import com.goodpon.application.couponissuer.service.accessor.UserCouponAccessor
import com.goodpon.application.couponissuer.service.exception.CouponTemplateNotFoundException
import com.goodpon.domain.coupon.template.CouponTemplateFactory
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.coupon.user.UserCoupon
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDate
import java.time.LocalDateTime

class IssueCouponServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val userCouponAccessor = mockk<UserCouponAccessor>()
    val couponHistoryAccessor = mockk<CouponHistoryAccessor>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)

    val issueCouponService = IssueCouponService(
        couponTemplateAccessor = couponTemplateAccessor,
        userCouponAccessor = userCouponAccessor,
        couponHistoryAccessor = couponHistoryAccessor,
        eventPublisher = eventPublisher
    )

    describe("IssueCouponService") {

        val command = IssueCouponCommand(
            couponTemplateId = 1L,
            userId = "testUser123",
            requestedAt = LocalDateTime.now()
        )

        val couponTemplateId = 1L
        val couponTemplate = CouponTemplateFactory.create(
            merchantId = 1L,
            name = "Test Coupon",
            description = "Test Description",
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 5000,
            maxDiscountAmount = null,
            issueStartDate = LocalDate.now().minusDays(1),
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
            issueAt = command.requestedAt
        )

        beforeTest {
            every { couponTemplateAccessor.readById(any()) } returns couponTemplate
            every { userCouponAccessor.existsByUserIdAndCouponTemplateId(any(), any()) } returns false
            every { userCouponAccessor.save(any()) } returns userCoupon
            every { couponHistoryAccessor.recordIssued(any(), any()) } returns mockk()
        }

        it("존재하지 않는 쿠폰 템플릿인 경우 예외를 발생시킨다.") {
            every { couponTemplateAccessor.readById(any()) } throws CouponTemplateNotFoundException()

            shouldThrow<CouponTemplateNotFoundException> {
                issueCouponService(command)
            }
        }

        it("사용자가 이미 쿠폰을 발급한 경우 다음 로직을 실행하지 않는다.") {
            every {
                userCouponAccessor.existsByUserIdAndCouponTemplateId(
                    command.userId,
                    command.couponTemplateId
                )
            } returns true


            verify(exactly = 0) { userCouponAccessor.save(any()) }
        }

        it("쿠폰을 발급하고 쿠폰 내역을 기록한다.") {
            issueCouponService(command)

            verify { userCouponAccessor.save(any()) }
            verify { couponHistoryAccessor.recordIssued(any(), any()) }
        }
    }
})