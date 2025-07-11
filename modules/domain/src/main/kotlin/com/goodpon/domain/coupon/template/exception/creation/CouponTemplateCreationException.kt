package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponTemplateCreationException(
    val errors: List<CouponTemplateCreationErrorDetail>,
) : BaseException("쿠폰 템플릿 생성에 실패했습니다.")