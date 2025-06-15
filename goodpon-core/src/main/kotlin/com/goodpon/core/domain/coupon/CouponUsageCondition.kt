package com.goodpon.core.domain.coupon

data class CouponUsageCondition private constructor(
    val minimumOrderAmount: Long? = null,
) {

    fun isSatisfiedBy(orderAmount: Long): Boolean {
        return minimumOrderAmount == null || orderAmount >= minimumOrderAmount
    }

    companion object {

        fun create(minimumOrderAmount: Long? = null): CouponUsageCondition {
            if (minimumOrderAmount != null && minimumOrderAmount <= 0) {
                throw IllegalArgumentException("최소 주문 금액은 0보다 커야 합니다.")
            }

            return CouponUsageCondition(minimumOrderAmount)
        }
    }
}