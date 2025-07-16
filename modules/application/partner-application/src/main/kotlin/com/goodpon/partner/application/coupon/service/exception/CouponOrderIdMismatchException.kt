package com.goodpon.partner.application.coupon.service.exception

import com.goodpon.domain.BaseException

class CouponOrderIdMismatchException : BaseException("쿠폰을 사용했던 주문과 쿠폰 사용 취소 시 주문이 일치하지 않습니다.")