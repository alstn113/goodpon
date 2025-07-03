package com.goodpon.core.domain.coupon.template.vo

import com.goodpon.core.domain.coupon.template.exception.CouponRedemptionConditionInvalidMinOrderAmountException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class CouponRedemptionConditionTest : DescribeSpec({
    describe("CouponRedemptionCondition 생성") {
        context("최소 주문 금액이 있을 경우") {
            it("최소 주문 금액은 0보다 커야 한다.") {
                forAll(
                    row(0),
                    row(-1)
                ) { minOrderAmount: Int ->
                    shouldThrow<CouponRedemptionConditionInvalidMinOrderAmountException> {
                        CouponRedemptionCondition(minOrderAmount)
                    }
                }
            }
        }
    }

    describe("isSatisfiedBy") {
        it("최소 주문 금액이 없는 경우 모든 주문 금액에 대해 만족한다.") {
            val condition = CouponRedemptionCondition()

            condition.isSatisfiedBy(0) shouldBe true
            condition.isSatisfiedBy(1000) shouldBe true
        }

        it("최소 주문 금액이 있는 경우 해당 금액 이상일 때만 만족한다.") {
            forAll(
                row(1000, 1001, true),
                row(1000, 1000, true),
                row(1000, 999, false),
            ) { minOrderAmount: Int, orderAmount: Int, expected: Boolean ->
                val condition = CouponRedemptionCondition(minOrderAmount)
                condition.isSatisfiedBy(orderAmount) shouldBe expected
            }
        }
    }
})