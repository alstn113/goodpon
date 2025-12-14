package com.goodpon.couponissuer.application.service.exception

import com.goodpon.domain.BaseException

class CouponTemplateNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 쿠폰 템플릿입니다.", cause)
