package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.support.error.BaseException

class CouponTemplateNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 쿠폰 템플릿입니다.", cause)