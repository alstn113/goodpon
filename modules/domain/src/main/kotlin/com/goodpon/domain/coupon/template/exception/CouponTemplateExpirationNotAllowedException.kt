package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.support.error.BaseException

class CouponTemplateExpirationNotAllowedException(
    cause: Throwable? = null,
) : BaseException("발급 가능한 쿠폰 템플릿이 아니면 만료할 수 없습니다.", cause)