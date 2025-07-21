package com.goodpon.application.partner.coupon.service.exception

import com.goodpon.domain.BaseException

class CouponTemplateStatsNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 쿠폰 템플릿 통계입니다.", cause)