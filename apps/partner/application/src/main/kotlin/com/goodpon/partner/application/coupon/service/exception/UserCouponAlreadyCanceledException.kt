package com.goodpon.partner.application.coupon.service.exception

import com.goodpon.domain.BaseException

class UserCouponAlreadyCanceledException(
    cause: Throwable? = null,
) : BaseException("이미 취소된 쿠폰입니다.", cause)