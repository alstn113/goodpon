package com.goodpon.core.domain.coupon.user.exception

import com.goodpon.core.support.error.BaseException

class UserCouponExpiredException(
    cause: Throwable? = null,
) : BaseException("쿠폰이 만료되어 사용할 수 없습니다.", cause)