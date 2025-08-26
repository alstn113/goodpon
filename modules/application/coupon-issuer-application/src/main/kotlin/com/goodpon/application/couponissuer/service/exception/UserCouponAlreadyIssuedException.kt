package com.goodpon.application.couponissuer.service.exception

import com.goodpon.domain.BaseException

class UserCouponAlreadyIssuedException(
    cause: Throwable? = null,
) : BaseException("이미 발급한 쿠폰입니다.", cause)