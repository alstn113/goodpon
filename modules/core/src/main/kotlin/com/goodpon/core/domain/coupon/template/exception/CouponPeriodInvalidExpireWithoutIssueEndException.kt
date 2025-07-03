package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponPeriodInvalidExpireWithoutIssueEndException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 절대 만료 시간은 발급 종료 시간이 설정된 경우에만 설정할 수 있습니다.", cause)