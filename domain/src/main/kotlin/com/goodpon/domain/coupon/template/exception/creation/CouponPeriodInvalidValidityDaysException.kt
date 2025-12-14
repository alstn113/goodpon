package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponPeriodInvalidValidityDaysException(
    val validityDays: Int,
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 유효 기간을 설정할 경우 0보다 커야 합니다.", cause)