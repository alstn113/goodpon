package com.goodpon.core.domain.coupon.template.vo

import com.goodpon.core.domain.coupon.template.exception.CouponDiscountPolicyInvalidFixedMaxException
import com.goodpon.core.domain.coupon.template.exception.CouponDiscountPolicyInvalidFixedValueException
import com.goodpon.core.domain.coupon.template.exception.CouponDiscountPolicyInvalidPercentMaxException
import com.goodpon.core.domain.coupon.template.exception.CouponDiscountPolicyInvalidPercentValueException

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
            throw CouponDiscountPolicyInvalidFixedValueException()
        }
        if (maxDiscountAmount != null) {
            throw CouponDiscountPolicyInvalidFixedMaxException()
        }
    }

    private fun validatePercentage() {
        if (discountValue !in 1..100) {
            throw CouponDiscountPolicyInvalidPercentValueException()
        }
        if (maxDiscountAmount == null || maxDiscountAmount <= 0) {
            throw CouponDiscountPolicyInvalidPercentMaxException()
        }
    }
}