package com.goodpon.common.domain.coupon

data class DiscountPolicy(
    val discountType: DiscountType,
    val discountValue: Int,
) {

    fun calculateDiscountAmount(orderAmount: Int): Int {
        val calculatedDiscount = when (discountType) {
            DiscountType.FIXED_AMOUNT -> discountValue
            DiscountType.PERCENTAGE -> (orderAmount * discountValue / 100.0).toInt()
        }

        return calculatedDiscount.coerceIn(0, orderAmount)
    }

    fun calculateFinalPrice(orderAmount: Int): Int {
        val discountAmount = calculateDiscountAmount(orderAmount)
        return (orderAmount - discountAmount).coerceAtLeast(0)
    }
}
