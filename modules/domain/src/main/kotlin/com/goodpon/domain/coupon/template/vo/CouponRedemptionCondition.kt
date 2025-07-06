package com.goodpon.domain.coupon.template.vo

data class CouponRedemptionCondition(
    val minOrderAmount: Int? = null,
) {

    init {
        validateMinOrderAmount(minOrderAmount)
    }

    fun isSatisfiedBy(orderAmount: Int): Boolean {
        return minOrderAmount == null || orderAmount >= minOrderAmount
    }

    private fun validateMinOrderAmount(minOrderAmount: Int? = null) {
        if (minOrderAmount != null && minOrderAmount <= 0) {
            throw com.goodpon.domain.coupon.template.exception.CouponRedemptionConditionInvalidMinOrderAmountException()
        }
    }
}