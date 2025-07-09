package com.goodpon.partner.application.coupon.service.exception

import com.goodpon.domain.BaseException

class UserCouponAlreadyIssuedException(
    cause: Throwable? = null,
) : BaseException("이미 발급한 쿠폰입니다.", cause)