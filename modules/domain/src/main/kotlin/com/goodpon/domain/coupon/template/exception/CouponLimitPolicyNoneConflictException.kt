package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.BaseException

class CouponLimitPolicyNoneConflictException(
    cause: Throwable? = null,
) : BaseException("무제한 정책의 쿠폰은 발급 제한 및 사용 제한을 설정할 수 없습니다.", cause)