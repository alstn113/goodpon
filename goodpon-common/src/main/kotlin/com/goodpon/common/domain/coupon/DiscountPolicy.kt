package com.goodpon.common.domain.coupon

data class DiscountPolicy(
    val discountType: DiscountType,
    val discountValue: Int,
    val minimumOrderAmount: Int? = null,
)


//