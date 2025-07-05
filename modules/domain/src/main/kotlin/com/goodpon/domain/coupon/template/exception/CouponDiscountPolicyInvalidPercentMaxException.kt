package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.support.error.BaseException

class CouponDiscountPolicyInvalidPercentMaxException(
    cause: Throwable? = null,
) : BaseException("백분율 할인의 경우 최대 할인 금액이 0보다 커야 합니다.", cause)