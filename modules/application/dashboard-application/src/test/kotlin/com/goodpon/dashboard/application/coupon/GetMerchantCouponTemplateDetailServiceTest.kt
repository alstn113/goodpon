package com.goodpon.dashboard.application.coupon

import com.goodpon.dashboard.application.coupon.port.`in`.dto.GetMerchantCouponTemplateDetailQuery
import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.dashboard.application.coupon.service.GetMerchantCouponTemplateDetailService
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.merchant.Merchant
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class GetMerchantCouponTemplateDetailServiceTest : DescribeSpec({

    val merchantAccessor = mockk<MerchantAccessor>()
    val couponTemplateRepository = mockk<CouponTemplateRepository>()

    val getMerchantCouponTemplateDetailService = GetMerchantCouponTemplateDetailService(
        merchantAccessor = merchantAccessor,
        couponTemplateRepository = couponTemplateRepository
    )

    val query = GetMerchantCouponTemplateDetailQuery(
        accountId = 1L,
        merchantId = 1L,
        couponTemplateId = 1L
    )

    val couponTemplateDetail = CouponTemplateDetail(
        id = 1L,
        merchantId = 1L,
        name = "테스트 쿠폰",
        description = "테스트 쿠폰 설명",
        minOrderAmount = 10000,
        discountType = CouponDiscountType.FIXED_AMOUNT,
        discountValue = 1000,
        maxDiscountAmount = null,
        status = CouponTemplateStatus.ISSUABLE,
        issueStartAt = LocalDate.of(2025, 7, 13).atStartOfDay(),
        issueEndAt = LocalDate.of(2025, 7, 21).atStartOfDay(),
        validityDays = null,
        absoluteExpiresAt = null,
        limitType = CouponLimitPolicyType.ISSUE_COUNT,
        maxIssueCount = 100L,
        maxRedeemCount = null,
        issueCount = 30,
        redeemCount = 10,
        createdAt = LocalDateTime.of(2025, 7, 13, 10, 0),
    )

    describe("getMerchantCouponTemplateDetail") {
        it("쿠폰 템플릿 상세 정보를 조회할 수 있다.") {
            // given
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = 1L,
            ).copy(id = 1L)
            every { merchantAccessor.readById(query.merchantId) } returns merchant
            every { couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId) } returns couponTemplateDetail

            // when
            val result = getMerchantCouponTemplateDetailService(query)

            // then
            result shouldBe couponTemplateDetail
        }

        it("상점이 없는 경우 예외를 발생시킨다") {
            // given
            every { merchantAccessor.readById(query.merchantId) } throws MerchantNotFoundException()

            // when & then
            shouldThrow<MerchantNotFoundException> {
                getMerchantCouponTemplateDetailService(query)
            }
        }

        it("상점에 대한 접근 권한이 없는 경우 예외를 발생시킨다") {
            // given
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = 2L, // 다른 계정의 상점
            ).copy(id = 1L)
            every { merchantAccessor.readById(query.merchantId) } returns merchant

            // when & then
            shouldThrow<NoMerchantAccessPermissionException> {
                getMerchantCouponTemplateDetailService(query)
            }
        }

        it("쿠폰 템플릿이 없는 경우 예외를 발생시킨다") {
            // given
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = 1L,
            ).copy(id = 1L)
            every { merchantAccessor.readById(query.merchantId) } returns merchant
            every { couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId) } returns null

            // when & then
            shouldThrow<CouponTemplateNotFoundException> {
                getMerchantCouponTemplateDetailService(query)
            }
        }

        it("상점이 소유한 쿠폰 템플릿이 아닌 경우 예외를 발생시킨다") {
            // given
            val merchant = Merchant.create(
                name = "Test Merchant",
                accountId = 1L,
            ).copy(id = 1L)
            val differentMerchantCouponTemplateDetail = couponTemplateDetail.copy(merchantId = 2L)
            every { merchantAccessor.readById(query.merchantId) } returns merchant
            every { couponTemplateRepository.findCouponTemplateDetail(query.couponTemplateId) } returns differentMerchantCouponTemplateDetail

            // when & then
            shouldThrow<CouponTemplateNotOwnedByMerchantException> {
                getMerchantCouponTemplateDetailService(query)
            }
        }
    }
})