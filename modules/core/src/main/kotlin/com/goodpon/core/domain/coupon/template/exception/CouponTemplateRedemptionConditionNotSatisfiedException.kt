package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponTemplateRedemptionConditionNotSatisfiedException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 조건을 만족하지 않습니다.", cause)