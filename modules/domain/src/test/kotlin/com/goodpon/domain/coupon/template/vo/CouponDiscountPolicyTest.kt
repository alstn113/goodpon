package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.creation.CouponDiscountPolicyInvalidFixedMaxException
import com.goodpon.domain.coupon.template.exception.creation.CouponDiscountPolicyInvalidFixedValueException
import com.goodpon.domain.coupon.template.exception.creation.CouponDiscountPolicyInvalidPercentMaxException
import com.goodpon.domain.coupon.template.exception.creation.CouponDiscountPolicyInvalidPercentValueException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.types.shouldBeInstanceOf

class CouponDiscountPolicyTest : DescribeSpec({

    describe("고정 금액 할인") {
        it("할인 값이 0 이하일 수 없다.") {
            val discountPolicy = CouponDiscountPolicy(CouponDiscountType.FIXED_AMOUNT, -1000)

            val result = discountPolicy.validate()

            result.exceptionOrNull().shouldBeInstanceOf<CouponDiscountPolicyInvalidFixedValueException>()
        }

        it("최대 할인 금액을 지정할 수 없다.") {
            val maxDiscountAmount = 500
            val discountPolicy = CouponDiscountPolicy(CouponDiscountType.FIXED_AMOUNT, 1000, maxDiscountAmount)

            val result = discountPolicy.validate()

            result.exceptionOrNull().shouldBeInstanceOf<CouponDiscountPolicyInvalidFixedMaxException>()
        }
    }

    describe("백분율 할인") {
        it("할인 값이 1 이상 100 이하여야 한다.") {
            forAll(
                row(-1),
                row(0),
                row(101),
            ) { discountValue ->
                val discountPolicy = CouponDiscountPolicy(CouponDiscountType.PERCENTAGE, discountValue)

                val result = discountPolicy.validate()

                result.exceptionOrNull().shouldBeInstanceOf<CouponDiscountPolicyInvalidPercentValueException>()
            }
        }

        it("최대 할인 금액이 없거나 0 이하일 수 없다.") {
            forAll(
                row(null),
                row(0),
                row(-1)
            ) { maxDiscountAmount ->
                val discountPolicy = CouponDiscountPolicy(CouponDiscountType.PERCENTAGE, 20, maxDiscountAmount)

                val result = discountPolicy.validate()

                result.exceptionOrNull().shouldBeInstanceOf<CouponDiscountPolicyInvalidPercentMaxException>()
            }
        }
    }
})