package com.goodpon.domain.coupon.template

import com.goodpon.domain.coupon.template.exception.creation.CouponTemplateValidationException
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class CouponTemplateFactoryTest : DescribeSpec({

    fun createCouponTemplate(
        merchantId: Long = 1L,
        minOrderAmount: Int? = 1000,
        discountType: CouponDiscountType = CouponDiscountType.FIXED_AMOUNT,
        discountValue: Int = 1000,
        maxDiscountAmount: Int? = null,
        issueStartDate: LocalDate = LocalDate.of(2025, 7, 3),
        issueEndDate: LocalDate? = LocalDate.of(2025, 7, 10),
        validityDays: Int? = null,
        absoluteExpiryDate: LocalDate? = LocalDate.of(2025, 7, 20),
        limitType: CouponLimitPolicyType = CouponLimitPolicyType.ISSUE_COUNT,
        maxIssueCount: Long? = 10L,
        maxRedeemCount: Long? = null,
        status: CouponTemplateStatus = CouponTemplateStatus.ISSUABLE,
    ): CouponTemplate {
        return CouponTemplateFactory.create(
            name = "테스트 쿠폰 템플릿",
            description = "테스트 쿠폰 템플릿 설명",
            merchantId = merchantId,
            minOrderAmount = minOrderAmount,
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount,
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            absoluteExpiryDate = absoluteExpiryDate,
            limitType = limitType,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = maxRedeemCount,
            status = status,
        )
    }

    describe("CouponTemplateFactory.create") {
        it("쿠폰 템플릿을 정상적으로 생성할 수 있다.") {
            shouldNotThrow<CouponTemplateValidationException> {
                CouponTemplateFactory.create(
                    merchantId = 1L,
                    name = "여름 할인 쿠폰",
                    description = "여름 한정 할인 쿠폰",
                    minOrderAmount = 1000,
                    discountType = CouponDiscountType.FIXED_AMOUNT,
                    discountValue = 500,
                    maxDiscountAmount = null,
                    issueStartDate = LocalDate.of(2025, 7, 1),
                    issueEndDate = LocalDate.of(2025, 7, 31),
                    validityDays = 10,
                    absoluteExpiryDate = LocalDate.of(2025, 8, 10),
                    limitType = CouponLimitPolicyType.ISSUE_COUNT,
                    maxIssueCount = 200L,
                    maxRedeemCount = null,
                    status = CouponTemplateStatus.DRAFT,
                )
            }
        }

        it("최소 주문 금액이 0 이하면 예외를 발생시키고, 예외 항목에 minOrderAmount가 포함된다.") {
            val exception = shouldThrow<CouponTemplateValidationException> {
                createCouponTemplate(
                    minOrderAmount = 0,
                    discountType = CouponDiscountType.FIXED_AMOUNT,
                )
            }
            exception.errors.any { it.field == "minOrderAmount" } shouldBe true
        }

        it("고정 할인인데 최대 할인 금액이 있으면 예외를 발생시키고, 예외 항목에 maxDiscountAmount가 포함된다.") {
            val exception = shouldThrow<CouponTemplateValidationException> {
                createCouponTemplate(
                    discountType = CouponDiscountType.FIXED_AMOUNT,
                    maxDiscountAmount = 1000,
                )
            }
            exception.errors.any { it.field == "maxDiscountAmount" } shouldBe true
        }

        it("백분율 할인인데 할인값이 1~100 사이가 아니면 예외를 발생시키고, 예외 항목에 discountValue가 포함된다.") {
            val exception = shouldThrow<CouponTemplateValidationException> {
                createCouponTemplate(
                    discountType = CouponDiscountType.PERCENTAGE,
                    discountValue = 101,
                )
            }
            exception.errors.any { it.field == "discountValue" } shouldBe true
        }

        it("쿠폰 발행 종료일이 쿠폰 발행 시작일보다 이전이면 예외를 발생시키고, 예외 항목에 issueEndDate가 포함된다.") {
            val exception = shouldThrow<CouponTemplateValidationException> {
                createCouponTemplate(
                    issueStartDate = LocalDate.of(2025, 7, 10),
                    issueEndDate = LocalDate.of(2025, 7, 9),
                )
            }
            exception.errors.any { it.field == "issueEndDate" } shouldBe true
        }

        it("쿠폰 유효 기간이 0일 이하이면 예외를 발생시키고, 예외 항목에 validityDays가 포함된다.") {
            val exception = shouldThrow<CouponTemplateValidationException> {
                createCouponTemplate(
                    validityDays = 0,
                )
            }
            exception.errors.any { it.field == "validityDays" } shouldBe true
        }

        it("발급 제한 정책이 설정됐지만 발급 제한 수량이 없으면 예외를 발생시키고, 예외 항목에 maxIssueCount가 포함된다.") {
            val exception = shouldThrow<CouponTemplateValidationException> {
                createCouponTemplate(
                    limitType = CouponLimitPolicyType.ISSUE_COUNT,
                    maxIssueCount = null,
                )
            }
            exception.errors.any { it.field == "maxIssueCount" } shouldBe true
        }

        it("여러 예외를 동시에 발생시킬 수 있다.") {
            val exception = shouldThrow<CouponTemplateValidationException> {
                createCouponTemplate(
                    minOrderAmount = 0,
                    discountType = CouponDiscountType.FIXED_AMOUNT,
                    maxDiscountAmount = 1000,
                    issueStartDate = LocalDate.of(2025, 7, 10),
                    issueEndDate = LocalDate.of(2025, 7, 9),
                    validityDays = 0,
                    limitType = CouponLimitPolicyType.ISSUE_COUNT,
                    maxIssueCount = null,
                )
            }
            exception.errors.any { it.field == "minOrderAmount" } shouldBe true
            exception.errors.any { it.field == "maxDiscountAmount" } shouldBe true
            exception.errors.any { it.field == "issueEndDate" } shouldBe true
            exception.errors.any { it.field == "maxIssueCount" } shouldBe true
        }
    }
})
