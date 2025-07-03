package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponTemplateStatusNotIssuableException(
    cause: Throwable? = null,
) : BaseException("쿠폰을 발급할 수 있는 상태가 아닙니다.", cause)