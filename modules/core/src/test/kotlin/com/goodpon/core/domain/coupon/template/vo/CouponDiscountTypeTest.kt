package com.goodpon.core.domain.coupon.template.vo

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CouponDiscountTypeTest : DescribeSpec({

    it("고정 금액 할인 타입은 할인 금액을 반환한다.") {
        val orderAmount = 5000
        val discountValue = 1000

        val discountAmount = CouponDiscountType.FIXED_AMOUNT.calculate(
            orderAmount = orderAmount,
            discountValue = discountValue,
            maxDiscountAmount = null
        )

        discountAmount shouldBe discountValue
    }

    it("백분율 할인 타입은 주문 금액에 백분율을 적용한 할인 금액을 반환한다.") {
        val orderAmount = 5000
        val discountValue = 20
        val maxDiscountAmount = 2000

        val discountAmount = CouponDiscountType.PERCENTAGE.calculate(
            orderAmount = orderAmount,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount
        )

        discountAmount shouldBe 1000
    }

    it("백분율 할인 타입은 최대 할인 금액을 초과하지 않는다.") {
        val orderAmount = 5000
        val discountValue = 50
        val maxDiscountAmount = 2000

        val discountAmount = CouponDiscountType.PERCENTAGE.calculate(
            orderAmount = orderAmount,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount
        )

        discountAmount shouldBe maxDiscountAmount
    }
})