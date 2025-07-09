package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.BaseException

class CouponTemplateIssuanceLimitExceededException(
    cause: Throwable? = null,
) : BaseException("쿠폰 발급 한도를 초과했습니다.", cause)