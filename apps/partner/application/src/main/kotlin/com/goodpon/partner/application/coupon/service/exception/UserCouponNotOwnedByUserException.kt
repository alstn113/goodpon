package com.goodpon.partner.application.coupon.service.exception

import com.goodpon.domain.BaseException

class UserCouponNotOwnedByUserException(
    cause: Throwable? = null,
) : BaseException("사용자가 보유한 쿠폰이 아닙니다.", cause)