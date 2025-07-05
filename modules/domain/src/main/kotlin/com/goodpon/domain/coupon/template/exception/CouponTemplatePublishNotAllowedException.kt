package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.support.error.BaseException

class CouponTemplatePublishNotAllowedException(
    cause: Throwable? = null,
) : BaseException("초안 상태의 쿠폰 템플릿만 발행할 수 있습니다.", cause)