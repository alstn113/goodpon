package com.goodpon.domain.application.coupon.exception

import com.goodpon.domain.support.error.BaseException

class CouponTemplateStatsNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 쿠폰 템플릿 통계입니다.", cause)