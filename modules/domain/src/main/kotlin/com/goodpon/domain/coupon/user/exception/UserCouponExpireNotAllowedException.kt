package com.goodpon.domain.coupon.user.exception

import com.goodpon.domain.support.error.BaseException

class UserCouponExpireNotAllowedException(
    cause: Throwable? = null,
) : BaseException("쿠폰을 만료할 수 있는 상태가 아닙니다.", cause)