package com.goodpon.core.domain.coupon.vo

import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType

data class CouponDiscountPolicy(
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int? = null,
) {
    init {
        when (discountType) {
            CouponDiscountType.FIXED_AMOUNT -> validateFixedAmount()
            CouponDiscountType.PERCENTAGE -> validatePercentage()
        }
    }

    fun calculateDiscountAmount(orderAmount: Int): Int {
        return discountType.calculate(
            orderAmount = orderAmount,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount
        )
    }

    private fun validateFixedAmount() {
        if (discountValue <= 0) {
            throw CoreException(ErrorType.INVALID_COUPON_POLICY_FIXED_AMOUNT_VALUE)
        }
        if (maxDiscountAmount != null) {
            throw CoreException(ErrorType.INVALID_COUPON_POLICY_FIXED_AMOUNT_MAX_AMOUNT)
        }
    }

    private fun validatePercentage() {
        if (discountValue !in 1..100) {
            throw CoreException(ErrorType.INVALID_COUPON_POLICY_PERCENTAGE_VALUE)
        }
        if (maxDiscountAmount == null || maxDiscountAmount <= 0) {
            throw CoreException(ErrorType.INVALID_COUPON_POLICY_PERCENTAGE_MAX_AMOUNT)
        }
    }
}