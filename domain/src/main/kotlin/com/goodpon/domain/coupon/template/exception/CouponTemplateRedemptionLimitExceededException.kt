package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.BaseException

class CouponTemplateRedemptionLimitExceededException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 한도를 초과했습니다.", cause)