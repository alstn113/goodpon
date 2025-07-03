package com.goodpon.core.domain.coupon.template.vo

import com.goodpon.core.domain.coupon.template.vo.CouponDiscountPolicy
import com.goodpon.core.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.core.support.error.CoreException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class CouponDiscountPolicyTest : DescribeSpec({
    describe("고정 금액 할인") {
        it("할인 값이 0 이하일 수 없다.") {
            val exception = shouldThrow<CoreException> {
                CouponDiscountPolicy(CouponDiscountType.FIXED_AMOUNT, -1000)
            }
            exception.errorType.message shouldBe "고정 금액 할인 값은 0보다 커야 합니다."
            exception.errorType.statusCode shouldBe 400
        }

        it("최대 할인 금액을 지정할 수 없다.") {
            val maxDiscountAmount = 500
            val exception = shouldThrow<CoreException> {
                CouponDiscountPolicy(CouponDiscountType.FIXED_AMOUNT, 1000, maxDiscountAmount)
            }
            exception.errorType.message shouldBe "고정 금액 할인은 최대 할인 금액을 설정할 수 없습니다."
            exception.errorType.statusCode shouldBe 400
        }
    }

    describe("백분율 할인") {
        it("할인 값이 1 이상 100 이하여야 한다.") {
            forAll(
                row(-1),
                row(0),
                row(101),
            ) { discountValue ->
                val exception = shouldThrow<CoreException> {
                    CouponDiscountPolicy(CouponDiscountType.PERCENTAGE, discountValue)
                }
                exception.errorType.message shouldBe "백분율 할인 값은 1에서 100 사이여야 합니다."
                exception.errorType.statusCode shouldBe 400
            }
        }

        it("최대 할인 금액이 없거나 0 이하일 수 없다.") {
            forAll(
                row(null),
                row(0),
                row(-1)
            ) { maxDiscountAmount ->
                val exception = shouldThrow<CoreException> {
                    CouponDiscountPolicy(CouponDiscountType.PERCENTAGE, 20, maxDiscountAmount)
                }
                exception.errorType.message shouldBe "백분율 할인의 경우 최대 할인 금액이 0보다 커야 합니다."
                exception.errorType.statusCode shouldBe 400
            }
        }
    }
})