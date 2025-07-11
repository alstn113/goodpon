package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.creation.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class CouponLimitPolicyTest : DescribeSpec({

    describe("쿠폰 제한 정책 생성") {
        context("발급 제한 정책") {
            it("발급 제한 수량이 없거나 0 이하일 수 없다.") {
                forAll(
                    row(null),
                    row(0L),
                    row(-1L)
                ) { maxIssueCount ->
                    val limitPolicy = CouponLimitPolicy(
                        limitType = CouponLimitPolicyType.ISSUE_COUNT,
                        maxIssueCount = maxIssueCount,
                        maxRedeemCount = null
                    )

                    val result = limitPolicy.validate()

                    result.exceptionOrNull().shouldBeInstanceOf<CouponLimitPolicyInvalidIssueValueException>()
                }
            }

            it("발급 제한 정책이 설정된 쿠폰은 사용 제한 수량을 함께 설정할 수 없다.") {
                val maxIssueCount = 10L
                val maxRedeemCount = 5L

                val limitPolicy = CouponLimitPolicy(
                    limitType = CouponLimitPolicyType.ISSUE_COUNT,
                    maxIssueCount = maxIssueCount,
                    maxRedeemCount = maxRedeemCount
                )

                val result = limitPolicy.validate()

                result.exceptionOrNull().shouldBeInstanceOf<CouponLimitPolicyIssueRedeemConflictException>()
            }
        }

        context("사용 제한 정책") {
            it("사용 제한 수량이 없거나 0 이하일 수 없다.") {
                forAll(
                    row(null),
                    row(0L),
                    row(-1L)
                ) { maxRedeemCount ->
                    val limitPolicy = CouponLimitPolicy(
                        limitType = CouponLimitPolicyType.REDEEM_COUNT,
                        maxIssueCount = null,
                        maxRedeemCount = maxRedeemCount
                    )

                    val result = limitPolicy.validate()

                    result.exceptionOrNull().shouldBeInstanceOf<CouponLimitPolicyInvalidRedeemValueException>()
                }
            }

            it("사용 제한 정책이 설정된 쿠폰은 발급 제한 수량을 함께 설정할 수 없다.") {
                val maxIssueCount = 10L
                val maxRedeemCount = 5L

                val limitPolicy = CouponLimitPolicy(
                    limitType = CouponLimitPolicyType.REDEEM_COUNT,
                    maxIssueCount = maxIssueCount,
                    maxRedeemCount = maxRedeemCount
                )

                val result = limitPolicy.validate()

                result.exceptionOrNull().shouldBeInstanceOf<CouponLimitPolicyRedeemIssueConflictException>()
            }
        }

        context("제한 정책이 없는 경우") {
            it("발급 제한 및 사용 제한을 설정할 수 없다.") {
                val maxIssueCountNotNull = 10L
                val maxRedeemCountNotNull = 5L

                val limitPolicy = CouponLimitPolicy(
                    limitType = CouponLimitPolicyType.NONE,
                    maxIssueCount = maxIssueCountNotNull,
                    maxRedeemCount = maxRedeemCountNotNull
                )

                val result = limitPolicy.validate()

                result.exceptionOrNull().shouldBeInstanceOf<CouponLimitPolicyNoneConflictException>()
            }
        }
    }

    describe("canIssue") {
        context("발급 제한 정책이 설정된 쿠폰") {
            it("현재 발급 수량에 따라 발급 가능 여부를 결정한다.") {
                val maxIssueCount = 10L

                forAll(
                    row(0L, true),
                    row(9L, true),
                    row(10L, false),
                ) { currentIssuedCount, expectedResult ->
                    val policy = CouponLimitPolicy(
                        limitType = CouponLimitPolicyType.ISSUE_COUNT,
                        maxIssueCount = maxIssueCount
                    )

                    val result = policy.canIssue(currentIssuedCount)

                    result shouldBe expectedResult
                }
            }
        }

        context("사용 제한 정책이 설정된 쿠폰") {
            it("사용 제한 정책에서는 발급 가능 여부를 확인하지 않는다") {
                val policy = CouponLimitPolicy(
                    limitType = CouponLimitPolicyType.REDEEM_COUNT,
                    maxRedeemCount = 5L
                )
                policy.canIssue(currentIssuedCount = 100L) shouldBe true
            }
        }

        context("무제한 정책이 설정된 쿠폰") {
            it("무제한 정책에서는 발급 가능 여부를 확인하지 않는다") {
                val policy =
                    CouponLimitPolicy(limitType = CouponLimitPolicyType.NONE)
                policy.canIssue(currentIssuedCount = 100L) shouldBe true
            }
        }
    }

    describe("canRedeem") {
        context("발급 제한 정책이 설정된 쿠폰") {
            it("발급 제한 정책에서는 사용 가능 여부를 확인하지 않는다") {
                val policy = CouponLimitPolicy(
                    limitType = CouponLimitPolicyType.ISSUE_COUNT,
                    maxIssueCount = 10L
                )
                policy.canRedeem(currentRedeemedCount = 100L) shouldBe true
            }
        }

        context("사용 제한 정책이 설정된 쿠폰") {
            it("현재 사용 수량에 따라 사용 가능 여부를 결정한다.") {
                val maxRedeemCount = 10L

                forAll(
                    row(0L, true),
                    row(9L, true),
                    row(10L, false),
                ) { currentRedeemedCount, expectedResult ->
                    val policy = CouponLimitPolicy(
                        limitType = CouponLimitPolicyType.REDEEM_COUNT,
                        maxRedeemCount = maxRedeemCount
                    )

                    val result = policy.canRedeem(currentRedeemedCount)

                    result shouldBe expectedResult
                }
            }
        }

        context("무제한 정책이 설정된 쿠폰") {
            it("무제한 정책에서는 사용 가능 여부를 확인하지 않는다") {
                val policy =
                    CouponLimitPolicy(limitType = CouponLimitPolicyType.NONE)
                policy.canRedeem(currentRedeemedCount = 100L) shouldBe true
            }
        }
    }
})