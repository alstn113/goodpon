package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.support.error.BaseException

class CouponDiscountPolicyInvalidPercentValueException(
    cause: Throwable? = null,
) : BaseException("백분율 할인 값은 1에서 100 사이여야 합니다.", cause)