package com.goodpon.application.dashboard.coupon.port.out.exception

import com.goodpon.domain.BaseException

class CouponTemplateNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 쿠폰 템플릿입니다.", cause)