package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.BaseException

class CouponTemplateStatusNotRedeemableException(
    cause: Throwable? = null,
) : BaseException("쿠폰을 사용할 수 있는 상태가 아닙니다.", cause)