package com.goodpon.dashboard.application.coupon

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.service.GetMerchantCouponTemplatesService
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.merchant.Merchant
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime


class GetMerchantCouponTemplatesServiceTest : DescribeSpec({

    val merchantAccessor = mockk<MerchantAccessor>()
    val couponTemplateRepository = mockk<CouponTemplateRepository>()
    val getMerchantCouponTemplatesService = GetMerchantCouponTemplatesService(
        merchantAccessor = merchantAccessor,
        couponTemplateRepository = couponTemplateRepository
    )

    describe("getMerchantCouponTemplates") {
        it("상점의 쿠폰 템플릿 목록을 조회할 수 있다.") {
            // given
            val accountId = 1L
            val merchantId = 1L
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = accountId,
            ).copy(id = merchantId)
            val couponTemplateSummaries = listOf(
                CouponTemplateSummary(
                    id = 1L,
                    name = "Test Coupon Template",
                    description = "Test Description",
                    status = CouponTemplateStatus.ISSUABLE,
                    issueCount = 100,
                    redeemCount = 50,
                    createdAt = LocalDateTime.now(),
                ),
            )

            every { merchantAccessor.readById(merchantId) } returns merchant
            every {
                couponTemplateRepository.findCouponTemplateSummaries(merchantId = merchantId)
            } returns couponTemplateSummaries

            // when
            val result = getMerchantCouponTemplatesService(
                accountId = accountId,
                merchantId = merchantId
            )

            // then
            result.templates shouldBe couponTemplateSummaries
        }

        it("상점이 존재하지 않으면 예외를 발생시킨다") {
            // given
            val accountId = 1L
            val merchantId = 1L

            every { merchantAccessor.readById(merchantId) } throws MerchantNotFoundException()

            // when, then
            shouldThrow<MerchantNotFoundException> {
                getMerchantCouponTemplatesService(
                    accountId = accountId,
                    merchantId = merchantId
                )
            }
        }

        it("상점에 대한 접근 권한이 없는 경우 예외를 발생시킨다") {
            // given
            val accountId = 2L // 다른 계정 ID
            val merchantId = 1L
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = 1L,
            ).copy(id = merchantId)

            every { merchantAccessor.readById(merchantId) } returns merchant

            // when, then
            shouldThrow<NoMerchantAccessPermissionException> {
                getMerchantCouponTemplatesService(
                    accountId = accountId,
                    merchantId = merchantId
                )
            }
        }
    }
})