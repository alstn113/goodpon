package com.goodpon.core.domain.coupon.user.exception

import com.goodpon.core.support.error.BaseException

class UserCouponCancelNotAllowedException(
    cause: Throwable? = null,
) : BaseException("쿠폰 사용을 취소할 수 있는 상태가 아닙니다.", cause)