package com.goodpon.application.partner.coupon.service.exception

import com.goodpon.domain.BaseException

class UserCouponNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 사용자 쿠폰입니다.", cause)