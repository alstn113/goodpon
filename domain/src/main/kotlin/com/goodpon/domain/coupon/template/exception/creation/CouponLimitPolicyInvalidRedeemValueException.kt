package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponLimitPolicyInvalidRedeemValueException(
    val maxRedeemCount: Long?,
    cause: Throwable? = null,
) : BaseException("사용 제한 정책이 설정된 쿠폰은 사용 제한 수량이 필요합니다.", cause)