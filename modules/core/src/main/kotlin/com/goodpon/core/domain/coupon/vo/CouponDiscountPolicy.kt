package com.goodpon.core.domain.coupon.vo

data class CouponDiscountPolicy(
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int? = null,
) {
    init {
        when (discountType) {
            CouponDiscountType.FIXED_AMOUNT -> {
                if (discountValue <= 0) {
                    throw IllegalArgumentException("고정 금액 할인은 0보다 커야 합니다.")
                }

                if (maxDiscountAmount != null) {
                    throw IllegalArgumentException("고정 금액 할인은 최대 할인 금액을 설정할 수 없습니다.")
                }
            }

            CouponDiscountType.PERCENTAGE -> {
                if (discountValue in 1..100) {
                    throw IllegalArgumentException("백분율 할인은 1에서 100 사이여야 합니다.")
                }

                if (maxDiscountAmount != null && maxDiscountAmount <= 0) {
                    throw IllegalArgumentException("백분율 할인은 최대 할인 금액이 0보다 커야 합니다.")
                }
            }
        }
    }

    fun calculateDiscountAmount(orderAmount: Int): Int {
        val calculatedDiscount = discountType.calculate(
            orderAmount = orderAmount,
            discountValue = discountValue
        )

        return when (discountType) {
            CouponDiscountType.FIXED_AMOUNT -> calculatedDiscount
            CouponDiscountType.PERCENTAGE -> minOf(calculatedDiscount, maxDiscountAmount!!)
        }
    }

    fun calculateFinalPrice(orderAmount: Int): Int {
        val discountAmount = calculateDiscountAmount(orderAmount)
        return orderAmount - discountAmount
    }
}
