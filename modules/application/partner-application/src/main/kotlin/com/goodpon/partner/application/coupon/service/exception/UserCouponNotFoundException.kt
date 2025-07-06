package com.goodpon.partner.application.coupon.service.exception

import com.goodpon.domain.support.error.BaseException

class UserCouponNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 사용자 쿠폰입니다.", cause)