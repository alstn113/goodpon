package com.goodpon.core.domain.legacy

import com.goodpon.core.domain.coupon.vo.CouponRedemptionCondition
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CouponRedemptionConditionTest : DescribeSpec({

    describe("constructor") {

        it("최소 주문 금액이 있을 경우 0보다 커야 한다.") {
            // when
            val exception = shouldThrow<IllegalArgumentException> {
                CouponRedemptionCondition(minOrderAmount = 0)
            }

            // then
            exception.message shouldBe "최소 주문 금액은 0보다 커야 합니다."
        }
    }

    describe("isSatisfiedBy") {

        it("최소 주문 금액이 없을 경우, 주문 금액에 관계없이 true를 반환한다.") {
            // given
            val condition = CouponRedemptionCondition(minOrderAmount = null)

            // when
            val result = condition.isSatisfiedBy(orderAmount = 1000)

            // then
            result shouldBe true
        }

        context("최소 주문 금액이 있는 경우") {

            it("주문 금액이 최소 주문 금액보다 크거나 같으면 true를 반환한다.") {
                // given
                val condition = CouponRedemptionCondition(minOrderAmount = 1000)

                // when
                val result = condition.isSatisfiedBy(orderAmount = 1000)

                // then
                result shouldBe true
            }

            it("주문 금액이 최소 주문 금액보다 작으면 false를 반환한다.") {
                // given
                val condition = CouponRedemptionCondition(minOrderAmount = 1000)

                // when
                val result = condition.isSatisfiedBy(orderAmount = 999)

                // then
                result shouldBe false
            }
        }
    }
})