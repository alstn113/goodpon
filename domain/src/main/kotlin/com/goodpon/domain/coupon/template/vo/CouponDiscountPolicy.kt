package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.creation.CouponDiscountPolicyInvalidFixedMaxException
import com.goodpon.domain.coupon.template.exception.creation.CouponDiscountPolicyInvalidFixedValueException
import com.goodpon.domain.coupon.template.exception.creation.CouponDiscountPolicyInvalidPercentMaxException
import com.goodpon.domain.coupon.template.exception.creation.CouponDiscountPolicyInvalidPercentValueException

data class CouponDiscountPolicy(
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int? = null,
) {

    fun calculateDiscountAmount(orderAmount: Int): Int {
        return discountType.calculate(
            orderAmount = orderAmount,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount
        )
    }

    fun validate(): Result<Unit> {
        return when (discountType) {
            CouponDiscountType.FIXED_AMOUNT -> validateFixedAmount()
            CouponDiscountType.PERCENTAGE -> validatePercentage()
        }

    }

    private fun validateFixedAmount(): Result<Unit> {
        if (discountValue <= 0) {
            return Result.failure(CouponDiscountPolicyInvalidFixedValueException(discountValue = discountValue))
        }
        if (maxDiscountAmount != null) {
            return Result.failure(CouponDiscountPolicyInvalidFixedMaxException(maxDiscountAmount = maxDiscountAmount))
        }
        return Result.success(Unit)
    }

    private fun validatePercentage(): Result<Unit> {
        if (discountValue !in 1..100) {
            return Result.failure(CouponDiscountPolicyInvalidPercentValueException(discountValue = discountValue))
        }
        if (maxDiscountAmount == null || maxDiscountAmount <= 0) {
            return Result.failure(CouponDiscountPolicyInvalidPercentMaxException(maxDiscountAmount = maxDiscountAmount))
        }
        return Result.success(Unit)
    }
}