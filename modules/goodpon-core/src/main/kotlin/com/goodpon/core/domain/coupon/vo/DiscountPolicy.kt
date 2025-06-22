package com.goodpon.core.domain.coupon.vo

data class DiscountPolicy(
    val discountType: DiscountType,
    val discountValue: Int,
) {

    init {
        when (discountType) {
            DiscountType.FIXED_AMOUNT -> {
                if (discountValue <= 0) {
                    throw IllegalArgumentException("고정 금액 할인은 0보다 커야 합니다.")
                }
            }

            DiscountType.PERCENTAGE -> {
                if (discountValue in 1..100) {
                    throw IllegalArgumentException("백분율 할인은 1에서 100 사이여야 합니다.")
                }
            }
        }
    }

    fun calculateDiscountAmount(orderAmount: Int): Int {
        val calculatedDiscount = discountType.calculate(
            orderAmount = orderAmount,
            discountValue = discountValue
        )

        return calculatedDiscount.coerceIn(0, orderAmount)
    }

    fun calculateFinalPrice(orderAmount: Int): Int {
        val discountAmount = calculateDiscountAmount(orderAmount)
        return orderAmount - discountAmount
    }
}
