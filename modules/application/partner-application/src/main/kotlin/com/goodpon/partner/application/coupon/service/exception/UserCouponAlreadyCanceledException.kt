package com.goodpon.partner.application.coupon.service.exception

import com.goodpon.domain.BaseException

class UserCouponAlreadyCanceledException : BaseException("이미 취소된 쿠폰입니다.")