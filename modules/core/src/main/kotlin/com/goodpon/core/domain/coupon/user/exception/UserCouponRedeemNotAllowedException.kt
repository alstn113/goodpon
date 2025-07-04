package com.goodpon.core.domain.coupon.user.exception

import com.goodpon.core.support.error.BaseException

class UserCouponRedeemNotAllowedException(
    cause: Throwable? = null,
) : BaseException("쿠폰을 사용할 수 있는 상태가 아닙니다.", cause)