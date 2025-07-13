package com.goodpon.dashboard.application.coupon

import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.service.PublishCouponTemplateService
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.dashboard.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.domain.coupon.template.CouponTemplateFactory
import com.goodpon.domain.coupon.template.exception.CouponTemplateInvalidStatusToPublishException
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.merchant.Merchant
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class PublishCouponTemplateServiceTest : DescribeSpec({

    val merchantAccessor = mockk<MerchantAccessor>()
    val couponTemplateAccessor = mockk<CouponTemplateAccessor>()
    val publishCouponTemplateService = PublishCouponTemplateService(merchantAccessor, couponTemplateAccessor)

    beforeEach {
        clearAllMocks()
    }

    val couponTemplate = CouponTemplateFactory.create(
        merchantId = 1L,
        name = "Test Coupon Template",
        description = "Test Description",
        minOrderAmount = 10000,
        discountType = CouponDiscountType.FIXED_AMOUNT,
        discountValue = 1000,
        maxDiscountAmount = null,
        issueStartDate = LocalDate.now(),
        issueEndDate = null,
        validityDays = null,
        absoluteExpiryDate = null,
        limitType = CouponLimitPolicyType.NONE,
        maxIssueCount = null,
        maxRedeemCount = null,
        status = CouponTemplateStatus.DRAFT,
    )

    describe("publishCouponTemplate") {
        it("쿠폰 템플릿을 발행할 수 있다.") {
            // given
            val accountId = 1L
            val command = PublishCouponTemplateCommand(
                merchantId = 1L,
                couponTemplateId = 1L,
                accountId = accountId,
            )
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = accountId,
            ).copy(id = 1L)

            every { merchantAccessor.readById(command.merchantId) } returns merchant
            every { couponTemplateAccessor.readById(command.couponTemplateId) } returns couponTemplate
            every {
                couponTemplateAccessor.save(any())
            } returns couponTemplate.copy(id = 1L, status = CouponTemplateStatus.ISSUABLE)

            // when
            val result = publishCouponTemplateService.publishCouponTemplate(command)

            // then
            result.status shouldBe CouponTemplateStatus.ISSUABLE
        }

        it("상점이 존재하지 않을 경우 예외를 던진다.") {
            // given
            val command = PublishCouponTemplateCommand(
                merchantId = 1L,
                couponTemplateId = 1L,
                accountId = 1L,
            )

            every { merchantAccessor.readById(command.merchantId) } throws MerchantNotFoundException()

            // when, then
            shouldThrow<MerchantNotFoundException> {
                publishCouponTemplateService.publishCouponTemplate(command)
            }
        }

        it("상점에 대한 접근 권한이 없는 경우 예외를 던진다.") {
            // given
            val command = PublishCouponTemplateCommand(
                merchantId = 1L,
                couponTemplateId = 1L,
                accountId = 2L, // 다른 계정 ID
            )
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = 1L,
            ).copy(id = 1L)

            every { merchantAccessor.readById(command.merchantId) } returns merchant

            // when, then
            shouldThrow<NoMerchantAccessPermissionException> {
                publishCouponTemplateService.publishCouponTemplate(command)
            }
        }


        it("쿠폰 템플릿을 소유한 상점이 아닌 경우 예외를 던진다.") {
            // given
            val merchantId = 2L
            val couponTemplateMerchantId = 1L
            val command = PublishCouponTemplateCommand(
                merchantId = merchantId,
                couponTemplateId = 1L,
                accountId = 1L,
            )
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = 1L,
            ).copy(id = merchantId)

            every { merchantAccessor.readById(command.merchantId) } returns merchant
            every {
                couponTemplateAccessor.readById(command.couponTemplateId)
            } returns couponTemplate.copy(id = 1L, merchantId = couponTemplateMerchantId)

            // when, then
            shouldThrow<CouponTemplateNotOwnedByMerchantException> {
                publishCouponTemplateService.publishCouponTemplate(command)
            }
        }

        it("쿠폰 템플릿이 발행 가능한 상태가 아닐 경우 예외를 던진다.") {
            // given
            val command = PublishCouponTemplateCommand(
                merchantId = 1L,
                couponTemplateId = 1L,
                accountId = 1L,
            )
            val invalidCouponTemplate = couponTemplate.copy(id = 1L, status = CouponTemplateStatus.ISSUABLE)

            every {
                merchantAccessor.readById(command.merchantId)
            } returns Merchant.create(name = "Test Merchant", accountId = 1L).copy(id = 1L)
            every { couponTemplateAccessor.readById(command.couponTemplateId) } returns invalidCouponTemplate

            // when, then
            shouldThrow<CouponTemplateInvalidStatusToPublishException> {
                publishCouponTemplateService.publishCouponTemplate(command)
            }
        }
    }
})