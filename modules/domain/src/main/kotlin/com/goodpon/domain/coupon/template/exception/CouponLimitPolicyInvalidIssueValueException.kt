package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.support.error.BaseException

class CouponLimitPolicyInvalidIssueValueException(
    cause: Throwable? = null,
) : BaseException("발급 제한 정책이 설정된 쿠폰은 발급 제한 수량이 필요합니다.", cause)