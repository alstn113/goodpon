package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.support.error.BaseException

class CouponPeriodInvalidExpireBeforeIssueEndException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 절대 만료 일시는 발급 종료 일시 이후이거나 같아야 합니다.", cause)