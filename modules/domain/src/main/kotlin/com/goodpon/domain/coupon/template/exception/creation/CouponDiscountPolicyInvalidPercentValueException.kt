package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponDiscountPolicyInvalidPercentValueException(
    val discountValue: Int,
    cause: Throwable? = null,
) : BaseException("백분율 할인 값은 1에서 100 사이여야 합니다.", cause)