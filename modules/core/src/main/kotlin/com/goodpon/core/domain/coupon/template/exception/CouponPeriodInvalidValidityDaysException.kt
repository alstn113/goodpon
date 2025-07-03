package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponPeriodInvalidValidityDaysException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 유효 기간을 설정할 경우 0보다 커야 합니다.", cause)