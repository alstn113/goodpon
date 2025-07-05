package com.goodpon.core.application.coupon.exception

import com.goodpon.core.support.error.BaseException

class UserCouponAlreadyIssuedException(
    cause: Throwable? = null,
) : BaseException("이미 발급한 쿠폰입니다.", cause)