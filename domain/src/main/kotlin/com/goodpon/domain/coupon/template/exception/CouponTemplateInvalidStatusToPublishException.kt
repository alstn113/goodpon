package com.goodpon.domain.coupon.template.exception

import com.goodpon.domain.BaseException

class CouponTemplateInvalidStatusToPublishException(
    cause: Throwable? = null,
) : BaseException("쿠폰 템플릿을 발핼할 수 있는 상태가 아닙니다.", cause)