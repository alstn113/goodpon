package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponDiscountPolicyInvalidFixedMaxException(
    val maxDiscountAmount: Int,
    cause: Throwable? = null,
) : BaseException("고정 금액 할인은 최대 할인 금액을 설정할 수 없습니다.", cause)