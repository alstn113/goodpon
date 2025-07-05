package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.support.error.BaseException

class CouponPeriodInvalidIssueEndBeforeStartException(
    cause: Throwable? = null,
) : BaseException("발급 종료 일시는 발급 시작 일시 이후여야 합니다.", cause)