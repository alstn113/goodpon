package com.goodpon.domain.application.coupon.exception

class CouponHistoryLastActionTypeNotRedeemException(
    cause: Throwable? = null,
) : Exception("마지막 쿠폰 사용 이력이 '사용됨'이 아닙니다.", cause)