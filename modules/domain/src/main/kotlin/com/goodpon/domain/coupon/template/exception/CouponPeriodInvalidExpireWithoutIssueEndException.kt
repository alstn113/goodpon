package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.BaseException

class CouponPeriodInvalidExpireWithoutIssueEndException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 절대 만료 일시는 발급 종료 일시가 설정된 경우에만 설정할 수 있습니다.", cause)