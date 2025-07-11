package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.creation.CouponRedemptionConditionInvalidMinOrderAmountException

data class CouponRedemptionCondition(
    val minOrderAmount: Int? = null,
) {

    fun isSatisfiedBy(orderAmount: Int): Boolean {
        return minOrderAmount == null || orderAmount >= minOrderAmount
    }

    fun validate(): Result<Unit> {
        if (minOrderAmount != null && minOrderAmount <= 0) {
            return Result.failure(CouponRedemptionConditionInvalidMinOrderAmountException(minOrderAmount = minOrderAmount))
        }
        return Result.success(Unit)
    }
}