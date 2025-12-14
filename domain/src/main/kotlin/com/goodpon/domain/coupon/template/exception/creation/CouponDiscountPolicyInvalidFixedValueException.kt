package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponDiscountPolicyInvalidFixedValueException(
    val discountValue: Int,
    cause: Throwable? = null,
) : BaseException("고정 금액 할인 값은 0보다 커야 합니다.", cause)