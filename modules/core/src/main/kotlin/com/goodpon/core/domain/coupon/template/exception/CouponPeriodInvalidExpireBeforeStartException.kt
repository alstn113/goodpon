package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponPeriodInvalidExpireBeforeStartException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 절대 만료 일시는 발급 시작 일시 이후여야 합니다.", cause)