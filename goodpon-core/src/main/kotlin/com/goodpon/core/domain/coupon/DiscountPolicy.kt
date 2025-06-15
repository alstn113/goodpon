package com.goodpon.core.domain.coupon

data class DiscountPolicy private constructor(
    val discountType: DiscountType,
    val discountValue: Int,
) {

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

    companion object {

        fun create(discountType: DiscountType, discountValue: Int): DiscountPolicy {
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

            return DiscountPolicy(
                discountType = discountType,
                discountValue = discountValue
            )
        }
    }
}
