package com.goodpon.application.partner.coupon.service.exception

import com.goodpon.domain.BaseException

class UserCouponCancelRedemptionNotAllowedException(
    cause: Throwable? = null,
) : BaseException("쿠폰을 사용 취소할 수 있는 상태가 아닙니다.", cause)