package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponRedemptionConditionInvalidMinOrderAmountException(
    cause: Throwable? = null,
) : BaseException("쿠폰을 사용하기 위한 최소 주문 금액을 설정할 경우 0보다 커야 합니다.", cause)