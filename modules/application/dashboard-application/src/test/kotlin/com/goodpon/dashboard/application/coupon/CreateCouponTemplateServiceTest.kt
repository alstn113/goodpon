package com.goodpon.dashboard.application.coupon

import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.service.CreateCouponTemplateService
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateStatsAccessor
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.domain.coupon.template.CouponTemplateFactory
import com.goodpon.domain.coupon.template.exception.creation.CouponTemplateValidationException
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.merchant.Merchant
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

class CreateCouponTemplateServiceTest : DescribeSpec({

    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val merchantAccessor = mockk<MerchantAccessor>()
    val couponTemplateStatsAccessor = mockk<CouponTemplateStatsAccessor>(relaxed = true)
    val createCouponTemplateService = CreateCouponTemplateService(
        couponTemplateAccessor,
        merchantAccessor,
        couponTemplateStatsAccessor
    )

    beforeEach {
        clearAllMocks()
    }

    val command = CreateCouponTemplateCommand(
        accountId = 1L,
        name = "쿠폰 템플릿 이름",
        description = "쿠폰 템플릿 설명",
        merchantId = 1L,
        minOrderAmount = 10000,
        discountType = CouponDiscountType.FIXED_AMOUNT,
        discountValue = 4000,
        maxDiscountAmount = null,
        issueStartDate = LocalDate.now(),
        issueEndDate = LocalDate.now().plusDays(30),
        validityDays = 10,
        absoluteExpiryDate = LocalDate.now().plusDays(40),
        limitType = CouponLimitPolicyType.ISSUE_COUNT,
        maxIssueCount = 1000,
        maxRedeemCount = null
    )

    val couponTemplate = CouponTemplateFactory.create(
        name = command.name,
        description = command.description,
        merchantId = command.merchantId,
        minOrderAmount = command.minOrderAmount,
        discountType = command.discountType,
        discountValue = command.discountValue,
        maxDiscountAmount = command.maxDiscountAmount,
        issueStartDate = command.issueStartDate,
        issueEndDate = command.issueEndDate,
        validityDays = command.validityDays,
        absoluteExpiryDate = command.absoluteExpiryDate,
        limitType = command.limitType,
        maxIssueCount = command.maxIssueCount,
        maxRedeemCount = command.maxRedeemCount
    ).copy(id = 1L)

    describe("createCouponTemplate") {
        it("상점에 쿠폰 템플릿을 생성한다.") {
            // given
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = command.accountId
            )
            every { merchantAccessor.readById(command.merchantId) } returns merchant
            every { couponTemplateAccessor.save(any()) } returns couponTemplate

            // when
            val result = createCouponTemplateService.createCouponTemplate(command)

            result.id shouldBe couponTemplate.id
            result.name shouldBe command.name

            verify { merchantAccessor.readById(command.merchantId) }
            verify { couponTemplateAccessor.save(any()) }
            verify { couponTemplateStatsAccessor.save(any()) }
        }


        it("존재하지 않는 상점일 경우 예외를 발생시킨다.") {
            every { merchantAccessor.readById(command.merchantId) } throws MerchantNotFoundException()

            shouldThrow<MerchantNotFoundException> {
                createCouponTemplateService.createCouponTemplate(command)
            }
        }

        it("상점에 접근할 수 없는 계정일 경우 예외를 발생시킨다.") {
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = 2L
            )
            every { merchantAccessor.readById(command.merchantId) } returns merchant

            shouldThrow<NoMerchantAccessPermissionException> {
                createCouponTemplateService.createCouponTemplate(command)
            }
        }

        it("쿠폰 템플릿 생성에 실패한 경우 예외를 발생시킨다.") {
            val invalidCommand = command.copy(
                discountType = CouponDiscountType.FIXED_AMOUNT,
                discountValue = -1000
            )
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = invalidCommand.accountId
            )
            every { merchantAccessor.readById(invalidCommand.merchantId) } returns merchant

            shouldThrow<CouponTemplateValidationException> {
                createCouponTemplateService.createCouponTemplate(invalidCommand)
            }
        }
    }
})