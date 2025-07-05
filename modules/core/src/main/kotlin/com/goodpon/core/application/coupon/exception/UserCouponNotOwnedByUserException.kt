package com.goodpon.core.application.coupon.exception

import com.goodpon.core.support.error.BaseException

class UserCouponNotOwnedByUserException(
    cause: Throwable? = null,
) : BaseException("사용자가 보유한 쿠폰이 아닙니다.", cause)