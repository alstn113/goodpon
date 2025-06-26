package com.goodpon.core.domain.coupon.vo

data class CouponRedemptionCondition(
    val minOrderAmount: Long? = null,
) {

    init {
        if (minOrderAmount != null && minOrderAmount <= 0) {
            throw IllegalArgumentException("최소 주문 금액은 0보다 커야 합니다.")
        }
    }

    fun isSatisfiedBy(orderAmount: Long): Boolean {
        return minOrderAmount == null || orderAmount >= minOrderAmount
    }
}