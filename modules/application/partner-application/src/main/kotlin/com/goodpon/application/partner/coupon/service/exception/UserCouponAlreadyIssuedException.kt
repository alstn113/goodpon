package com.goodpon.application.partner.coupon.service.exception

import com.goodpon.domain.BaseException

class UserCouponAlreadyIssuedException(
    cause: Throwable? = null,
) : BaseException("이미 발급한 쿠폰입니다.", cause)