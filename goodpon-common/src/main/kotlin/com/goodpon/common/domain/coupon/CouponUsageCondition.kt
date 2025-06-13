package com.goodpon.common.domain.coupon

data class CouponUsageCondition(
    val minimumOrderAmount: Long? = null,
) {

    init {
        if (minimumOrderAmount != null && minimumOrderAmount <= 0) {
            throw IllegalArgumentException("최소 주문 금액은 0보다 커야 합니다.")
        }
    }

    fun isSatisfiedBy(orderAmount: Long): Boolean {
        return minimumOrderAmount == null || orderAmount >= minimumOrderAmount
    }
}