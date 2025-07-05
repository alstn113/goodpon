package com.goodpon.core.domain.coupon.template

import com.goodpon.core.domain.coupon.template.exception.*
import com.goodpon.core.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.core.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.core.domain.coupon.template.vo.CouponTemplateStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class CouponTemplateTest : DescribeSpec({

    val issueAt = LocalDateTime.of(2025, 7, 5, 12, 30)

    describe("validateIssue") {
        it("쿠폰을 발급할 수 없는 기간이면 예외를 발생시킨다.") {
            val template = createCouponTemplate(
                issueStartDate = LocalDate.of(2025, 7, 3),
                issueEndDate = LocalDate.of(2025, 7, 10)
            )

            val cannotIssueAt = LocalDateTime.of(2025, 7, 2, 12, 30)

            shouldThrow<CouponTemplateIssuancePeriodException> {
                template.validateIssue(currentIssuedCount = 0, issueAt = cannotIssueAt)
            }
        }

        it("쿠폰 템플릿이 발급 가능한 상태가 아니면 예외를 발생시킨다.") {
            forAll(
                row(CouponTemplateStatus.DRAFT),
                row(CouponTemplateStatus.EXPIRED),
                row(CouponTemplateStatus.DISCARDED)
            ) { status ->
                val template = createCouponTemplate(status = status)

                shouldThrow<CouponTemplateStatusNotIssuableException> {
                    template.validateIssue(currentIssuedCount = 0, issueAt = issueAt)
                }
            }
        }

        it("발급 한도를 초과하면 예외를 발생시킨다.") {
            val maxIssueCount = 10L
            val template = createCouponTemplate(
                limitType = CouponLimitPolicyType.ISSUE_COUNT,
                maxIssueCount = maxIssueCount,
            )

            val currentIssuedCount = maxIssueCount + 1
            shouldThrow<CouponTemplateIssuanceLimitExceededException> {
                template.validateIssue(currentIssuedCount = currentIssuedCount, issueAt = issueAt)
            }
        }
    }

    describe("validateRedeem") {
        it("쿠폰 템플릿이 쿠폰 사용 불가 상태이면 예외를 발생시킨다.") {
            val template = createCouponTemplate(
                status = CouponTemplateStatus.DRAFT
            )

            shouldThrow<CouponTemplateStatusNotRedeemableException> {
                template.validateRedeem(currentRedeemedCount = 0, orderAmount = 5000)
            }
        }

        it("쿠폰 사용 한도를 초과하면 예외를 발생시킨다.") {
            val maxRedeemCount = 5L
            val template = createCouponTemplate(
                limitType = CouponLimitPolicyType.REDEEM_COUNT,
                maxIssueCount = null,
                maxRedeemCount = maxRedeemCount,
            )

            val currentRedeemedCount = maxRedeemCount + 1
            shouldThrow<CouponTemplateRedemptionLimitExceededException> {
                template.validateRedeem(currentRedeemedCount = currentRedeemedCount, orderAmount = 5000)
            }
        }

        it("쿠폰 사용 조건을 만족하지 않으면 예외를 발생시킨다.") {
            val minOrderAmount = 1000
            val orderAmount = 500

            val template = createCouponTemplate(minOrderAmount = minOrderAmount)

            shouldThrow<CouponTemplateRedemptionConditionNotSatisfiedException> {
                template.validateRedeem(currentRedeemedCount = 0, orderAmount = orderAmount)
            }
        }
    }

    describe("isOwnedBy") {
        it("쿠폰 템플릿이 특정 상점에 속하는지 확인할 수 있다.") {
            val template = createCouponTemplate()

            val isOwned = template.isOwnedBy(1L)

            isOwned shouldBe true
        }

        it("다른 상점의 쿠폰 템플릿은 소유하지 않은 것으로 간주된다.") {
            val template = createCouponTemplate()

            val isOwned = template.isOwnedBy(2L)

            isOwned shouldBe false
        }

    }

    describe("calculateExpiresAt") {
        it("쿠폰의 만료 일시를 계산할 수 있다.") {
            val template = createCouponTemplate(
                issueStartDate = LocalDate.of(2025, 7, 3),
                issueEndDate = LocalDate.of(2025, 7, 10),
                validityDays = 5,
                absoluteExpiryDate = null
            )

            val expiresAt = template.calculateExpiresAt(LocalDate.of(2025, 7, 5))

            expiresAt shouldBe LocalDate.of(2025, 7, 11).atStartOfDay()
        }
    }

    describe("calculateDiscountAmount") {
        it("할인 금액을 계산할 수 있다.") {
            val template = createCouponTemplate(
                discountType = CouponDiscountType.FIXED_AMOUNT,
                discountValue = 1000
            )

            val discountAmount = template.calculateDiscountAmount(5000)

            discountAmount shouldBe 1000
        }
    }

    describe("calculateFinalPrice") {
        it("할인된 최종 가격을 계산할 수 있다.") {
            val template = createCouponTemplate(
                discountType = CouponDiscountType.FIXED_AMOUNT,
                discountValue = 1000
            )

            val finalPrice = template.calculateFinalPrice(5000)

            finalPrice shouldBe 4000
        }
    }

    describe("publish") {
        it("쿠폰 템플릿을 발행할 수 있다.") {
            val template = createCouponTemplate(status = CouponTemplateStatus.DRAFT)

            val publishedTemplate = template.publish()

            publishedTemplate.status shouldBe CouponTemplateStatus.ISSUABLE
        }

        it("초안 상태의 쿠폰 템플릿만 발행할 수 있다.") {
            forAll(
                row(CouponTemplateStatus.ISSUABLE),
                row(CouponTemplateStatus.EXPIRED),
                row(CouponTemplateStatus.DISCARDED)
            ) { status ->
                val template = createCouponTemplate(status = status)

                shouldThrow<CouponTemplatePublishNotAllowedException> {
                    template.publish()
                }
            }
        }
    }

    describe("expire") {
        it("쿠폰 템플릿을 만료시킬 수 있다.") {
            val template = createCouponTemplate(
                status = CouponTemplateStatus.ISSUABLE
            )

            val expiredTemplate = template.expire()

            expiredTemplate.status shouldBe CouponTemplateStatus.EXPIRED
        }

        it("만료되지 않은 쿠폰 템플릿은 만료할 수 없다.") {
            forAll(
                row(CouponTemplateStatus.DRAFT),
                row(CouponTemplateStatus.EXPIRED),
                row(CouponTemplateStatus.DISCARDED)
            ) { status ->
                val template = createCouponTemplate(status = status)

                shouldThrow<CouponTemplateExpirationNotAllowedException> {
                    template.expire()
                }
            }
        }
    }
})

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
        status = status
    )
}