package com.goodpon.application.partner.coupon

import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.out.CouponEventPublisher
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.port.out.dto.IssueResult
import com.goodpon.application.partner.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.application.partner.coupon.service.IssueCouponService
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class IssueCouponServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val couponTemplateStatsCache = mockk<CouponTemplateStatsCache>()
    val couponEventPublisher = mockk<CouponEventPublisher>()

    val issueCouponService = IssueCouponService(
        couponTemplateAccessor = couponTemplateAccessor,
        couponTemplateStatsCache = couponTemplateStatsCache,
        couponEventPublisher = couponEventPublisher
    )

    describe("IssueCouponService") {

        val couponTemplateId = 1L
        val command = IssueCouponCommand(
            couponTemplateId = couponTemplateId,
            userId = "testUser123",
            merchantId = 100L
        )
        val couponTemplate = mockk<CouponTemplate>()

        beforeTest {
            every { couponTemplateAccessor.readById(couponTemplateId) } returns couponTemplate
            every { couponTemplate.id } returns couponTemplateId
            every { couponTemplate.isOwnedBy(command.merchantId) } returns true
            every { couponTemplate.validateIssue(any()) } returns Unit
            every { couponTemplate.maxIssueCount() } returns 10
            every { couponTemplateStatsCache.issueCoupon(any(), any(), any()) } returns IssueResult.SUCCESS
            every { couponEventPublisher.publishIssueCouponRequested(any()) } returns Unit
        }

        it("상점이 존재하지 않는 경우 예외를 던진다") {
            every { couponTemplateAccessor.readById(couponTemplateId) } throws CouponTemplateNotFoundException()

            shouldThrow<CouponTemplateNotFoundException> {
                issueCouponService(command)
            }
        }

        it("상점이 소유하지 않은 쿠폰 템플릿인 경우 예외를 던진다") {
            every { couponTemplate.isOwnedBy(command.merchantId) } returns false

            shouldThrow<CouponTemplateNotOwnedByMerchantException> {
                issueCouponService(command)
            }
        }

        it("이미 발급받은 사용자 쿠폰이 있는 경우 예외를 던진다") {
            every { couponTemplateStatsCache.issueCoupon(any(), any(), any()) } returns IssueResult.ALREADY_ISSUED

            shouldThrow<UserCouponAlreadyIssuedException> {
                issueCouponService(command)
            }

            verify(exactly = 0) {
                couponEventPublisher.publishIssueCouponRequested(any())
            }
        }

        it("발급 제한을 초과한 경우 예외를 던진다") {
            every { couponTemplateStatsCache.issueCoupon(any(), any(), any()) } returns IssueResult.ISSUE_LIMIT_EXCEEDED

            shouldThrow<CouponTemplateIssuanceLimitExceededException> {
                issueCouponService(command)
            }

            verify(exactly = 0) {
                couponEventPublisher.publishIssueCouponRequested(any())
            }
        }

        it("정상적으로 쿠폰 발급 요청을 처리한다") {
            issueCouponService(command)

            verify { couponEventPublisher.publishIssueCouponRequested(any()) }
        }
    }
})