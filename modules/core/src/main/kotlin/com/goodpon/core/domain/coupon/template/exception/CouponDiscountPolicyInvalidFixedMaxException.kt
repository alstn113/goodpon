package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponDiscountPolicyInvalidFixedMaxException(
    cause: Throwable? = null,
) : BaseException("고정 금액 할인은 최대 할인 금액을 설정할 수 없습니다.", cause)