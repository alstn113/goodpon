package com.goodpon.common.domain.coupon

data class DiscountPolicy(
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
}
