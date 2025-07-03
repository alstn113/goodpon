package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponDiscountPolicyInvalidFixedValueException(
    cause: Throwable? = null,
) : BaseException("고정 금액 할인 값은 0보다 커야 합니다.", cause)