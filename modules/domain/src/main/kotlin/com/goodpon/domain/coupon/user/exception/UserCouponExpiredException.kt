package com.goodpon.domain.coupon.user.exception

import com.goodpon.domain.BaseException

class UserCouponExpiredException(
    cause: Throwable? = null,
) : BaseException("쿠폰이 만료되어 사용할 수 없습니다.", cause)