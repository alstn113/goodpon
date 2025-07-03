package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponLimitPolicyInvalidRedeemValueException(
    cause: Throwable? = null,
) : BaseException("사용 제한 정책이 설정된 쿠폰은 사용 제한 수량이 필요합니다.", cause)