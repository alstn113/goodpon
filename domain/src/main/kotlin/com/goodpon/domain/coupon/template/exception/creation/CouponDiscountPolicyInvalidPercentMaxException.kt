package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponDiscountPolicyInvalidPercentMaxException(
    val maxDiscountAmount: Int?,
    cause: Throwable? = null,
) : BaseException("백분율 할인의 경우 최대 할인 금액이 0보다 커야 합니다.", cause)