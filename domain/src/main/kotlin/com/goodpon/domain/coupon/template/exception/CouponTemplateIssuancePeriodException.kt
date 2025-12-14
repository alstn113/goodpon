package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.BaseException

class CouponTemplateIssuancePeriodException(
    cause: Throwable? = null,
) : BaseException("쿠폰을 발급할 수 있는 기간이 아닙니다.", cause)