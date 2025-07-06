package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.CouponDiscountPolicyInvalidFixedMaxException
import com.goodpon.domain.coupon.template.exception.CouponDiscountPolicyInvalidFixedValueException
import com.goodpon.domain.coupon.template.exception.CouponDiscountPolicyInvalidPercentMaxException
import com.goodpon.domain.coupon.template.exception.CouponDiscountPolicyInvalidPercentValueException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row

class CouponDiscountPolicyTest : DescribeSpec({

    describe("고정 금액 할인") {
        it("할인 값이 0 이하일 수 없다.") {
            shouldThrow<CouponDiscountPolicyInvalidFixedValueException> {
                CouponDiscountPolicy(CouponDiscountType.FIXED_AMOUNT, -1000)
            }
        }

        it("최대 할인 금액을 지정할 수 없다.") {
            val maxDiscountAmount = 500

            shouldThrow<CouponDiscountPolicyInvalidFixedMaxException> {
                CouponDiscountPolicy(CouponDiscountType.FIXED_AMOUNT, 1000, maxDiscountAmount)
            }
        }
    }

    describe("백분율 할인") {
        it("할인 값이 1 이상 100 이하여야 한다.") {
            forAll(
                row(-1),
                row(0),
                row(101),
            ) { discountValue ->
                shouldThrow<com.goodpon.domain.coupon.template.exception.CouponDiscountPolicyInvalidPercentValueException> {
                    CouponDiscountPolicy(CouponDiscountType.PERCENTAGE, discountValue)
                }
            }
        }

        it("최대 할인 금액이 없거나 0 이하일 수 없다.") {
            forAll(
                row(null),
                row(0),
                row(-1)
            ) { maxDiscountAmount ->
                shouldThrow<CouponDiscountPolicyInvalidPercentMaxException> {
                    CouponDiscountPolicy(CouponDiscountType.PERCENTAGE, 20, maxDiscountAmount)
                }
            }
        }
    }
})