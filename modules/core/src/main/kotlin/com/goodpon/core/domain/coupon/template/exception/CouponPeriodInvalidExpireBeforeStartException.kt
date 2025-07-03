package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponPeriodInvalidExpireBeforeStartException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 절대 만료 시간은 발급 시작 시간 이후여야 합니다.", cause)