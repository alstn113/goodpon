package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponTemplateValidationException(
    val errors: List<CouponTemplateValidationError>,
) : BaseException("쿠폰 템플릿 생성 요청이 유효하지 않습니다.")