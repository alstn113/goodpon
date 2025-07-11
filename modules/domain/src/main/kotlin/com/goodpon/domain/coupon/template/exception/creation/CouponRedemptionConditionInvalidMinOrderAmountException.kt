package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponRedemptionConditionInvalidMinOrderAmountException(
    val minOrderAmount: Int,
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 조건의 최소 주문 금액은 0보다 커야 합니다.", cause)