package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponTemplateIssuanceLimitExceededException(
    cause: Throwable? = null,
) : BaseException("쿠폰 발급 한도를 초과했습니다.", cause)